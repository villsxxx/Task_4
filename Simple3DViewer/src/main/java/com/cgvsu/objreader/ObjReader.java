package com.cgvsu.objreader;

import com.cgvsu.math.Vector2f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Group;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.exceptions.*;
import java.util.*;

public class ObjReader {

	private static final String OBJ_VERTEX_TOKEN = "v";
	private static final String OBJ_TEXTURE_TOKEN = "vt";
	private static final String OBJ_NORMAL_TOKEN = "vn";
	private static final String OBJ_FACE_TOKEN = "f";
	private static final String OBJ_GROUP_TOKEN = "g";
	private static final String COMMENT_TOKEN = "#";

	private final Model model = new Model();
	protected static boolean force = true;
	private static int lineInd = 0;
	private Group currentGroup = null;

	protected ObjReader() {}
	public static Model read(String model) {
		return read(model, true);
	}

	public static Model read(String model, boolean force) {
		ObjReader objReader = new ObjReader();
		ObjReader.force = force;
		objReader.readModel(model);
		return objReader.model;
	}

	protected void readModel(String fileContent) {
		Locale.setDefault(Locale.ROOT);
		Scanner scanner = new Scanner(fileContent);
		while (scanner.hasNextLine()) {
			//удаляю коменты
			String line = deleteCommentLine(scanner.nextLine().trim());
			//если пустая, пропускаю
			if (line.isBlank()) {
				continue;
			}

			ArrayList<String> wordsInLine = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
			if (wordsInLine.isEmpty()) {
				continue;
			}

			final String token = wordsInLine.get(0);
			wordsInLine.remove(0);

			++lineInd;
			switch (token) {
				case OBJ_VERTEX_TOKEN -> model.vertices.add(parseVertex(wordsInLine, lineInd));
				case OBJ_TEXTURE_TOKEN -> model.textureVertices.add(parseTextureVertex(wordsInLine, lineInd));
				case OBJ_NORMAL_TOKEN -> model.normals.add(parseNormal(wordsInLine, lineInd));
				case OBJ_FACE_TOKEN -> handleFace(wordsInLine);
				case OBJ_GROUP_TOKEN -> handleGroup(wordsInLine);
				default -> {
					if (!force) {
						throw new TokenException(lineInd);
					}
				}
			}
		}

		if (currentGroup != null) {
			model.addGroup(currentGroup);
		}

		int verticesSize = model.getVerticesSize();
		int textureVerticesSize = model.getTextureVerticesSize();
		int normalsSize = model.getNormalsSize();
		for (Polygon polygon: model.getPolygons()) {
			polygon.checkIndices(verticesSize, textureVerticesSize, normalsSize);
		}
	}
	private static String deleteCommentLine(String line) {

		int commentIndex = line.indexOf(COMMENT_TOKEN);
		if (commentIndex > -1) {
			line = line.substring(0, commentIndex);
		}
		return line;
	}

	// Всем методам кроме основного я поставил модификатор доступа protected, чтобы обращаться к ним в тестах
	protected static Vector3f parseVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2)));

		}  catch (ParsingException e) {
			throw new ParsingException("float", lineInd);
		}

	}

	protected static Vector2f parseTextureVertex(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector2f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)));

		} catch (ParsingException e) {
			throw new ParsingException("float", lineInd);
		}
	}

	protected static Vector3f parseNormal(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		try {
			return new Vector3f(
					Float.parseFloat(wordsInLineWithoutToken.get(0)),
					Float.parseFloat(wordsInLineWithoutToken.get(1)),
					Float.parseFloat(wordsInLineWithoutToken.get(2)));

		} catch (ParsingException e) {
			throw new ParsingException("float", lineInd);
		}
	}

	protected static Polygon parseFace(final ArrayList<String> wordsInLineWithoutToken, int lineInd) {
		List<FaceWord> faceWords = new ArrayList<>();
		Set<WordType> types = new HashSet<>();

		for (String word : wordsInLineWithoutToken) {
			FaceWord faceWord = FaceWord.parse(word, lineInd, force);
			types.add(faceWord.getWordType());
			faceWords.add(faceWord);
		}
		//минимум три
		if (faceWords.size() < 3) {
			throw new ArgumentsException(ArgumentsErrorType.FEW_IN_POLYGON, lineInd);
		}
		//один формат
		if (types.size() > 1) {
			throw new FaceTypeException(lineInd);
		}

		return createPolygon(faceWords.toArray(new FaceWord[0]));
	}
	//разные текстуры
	private void handleFace(final ArrayList<String> wordsInLineWithoutToken) {
		Polygon polygon = parseFace(wordsInLineWithoutToken, lineInd);

		if (!model.getPolygons().isEmpty()) {
			Polygon firstPolygon = model.getFirstPolygon();
			if (polygon.hasTexture() != firstPolygon.hasTexture()) {
				throw new TextureException(lineInd);
			}
		}

		model.addPolygon(polygon);
		if (currentGroup != null) {
			currentGroup.addPolygon(polygon);
		}
	}

	private void handleGroup(final ArrayList<String> wordsInLineWithoutToken) {
		if (wordsInLineWithoutToken.isEmpty()) {
			throw new GroupNameException(lineInd);
		}

		if (currentGroup != null) {
			model.addGroup(currentGroup);
		}

		StringBuilder sb = new StringBuilder();
		for (String s : wordsInLineWithoutToken) {
			sb.append(s).append(' ');
		}
		sb.deleteCharAt(sb.length() - 1);
		currentGroup = new Group(sb.toString());
	}

	protected static Polygon createPolygon(FaceWord[] faceWords) {
		Polygon polygon = new Polygon();
		ArrayList<Integer> vertexIndices = new ArrayList<>();
		ArrayList<Integer> textureVertexIndices = new ArrayList<>();
		ArrayList<Integer> normalIndices = new ArrayList<>();

		for (FaceWord faceWord : faceWords) {
			Integer vertexIndex = faceWord.getVertexIndex();
			if (vertexIndex != null) {
				vertexIndices.add(vertexIndex);
			}
			Integer textureVertexIndex = faceWord.getTextureVertexIndex();
			if (textureVertexIndex != null) {
				textureVertexIndices.add(textureVertexIndex);
			}
			Integer normalIndex = faceWord.getNormalIndex();
			if (normalIndex != null) {
				normalIndices.add(normalIndex);
			}
		}

		polygon.setVertexIndices(vertexIndices);
		polygon.setTextureVertexIndices(textureVertexIndices);
		polygon.setNormalIndices(normalIndices);
		polygon.setLineIndex(lineInd);

		return polygon;
	}
	//из шаблона
	private void checkForErrors(final ArrayList<String> wordsInLineWithoutToken, int lineInd, int size) {
		if (!force && wordsInLineWithoutToken == null) {
			throw new ArgumentsException(ArgumentsErrorType.NULL, lineInd);
		}
		checkSize(wordsInLineWithoutToken.size(), size);
	}

	private void checkSize(int wordCount, int vectorSize) {
		if (wordCount == vectorSize) {
			return;
		}
		if (wordCount < vectorSize) {
			throw new ArgumentsException(ArgumentsErrorType.FEW, lineInd);
		}
		if (!force) {
			throw new ArgumentsException(ArgumentsErrorType.MANY, lineInd);
		}
	}

	// Обратите внимание, что для чтения полигонов я выделил еще один вспомогательный метод.
	// Это бывает очень полезно и с точки зрения структурирования алгоритма в голове, и с точки зрения тестирования.
	// В радикальных случаях не бойтесь выносить в отдельные методы и тестировать код из одной-двух строчек.
	protected static void parseFaceWord(
			String wordInLine,
			ArrayList<Integer> onePolygonVertexIndices,
			ArrayList<Integer> onePolygonTextureVertexIndices,
			ArrayList<Integer> onePolygonNormalIndices,
			int lineInd) {
		try {
			String[] wordIndices = wordInLine.split("/");
			switch (wordIndices.length) {
				case 1 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
				}
				case 2 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
					onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
				}
				case 3 -> {
					onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
					onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
					if (!wordIndices[1].equals("")) {
						onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
					}
				}
				default -> {
					throw new ObjReaderException("Invalid element size.", lineInd);
				}
			}

		} catch(NumberFormatException e) {
			throw new ObjReaderException("Failed to parse int value.", lineInd);

		} catch(IndexOutOfBoundsException e) {
			throw new ObjReaderException("Too few arguments.", lineInd);
		}
	}
}
