package com.cgvsu.model;

import com.cgvsu.math.AffineTransforms;
import com.cgvsu.math.Matrix4f;

public class Transformation {
    private static final Matrix4f DEFAULT_SCALE = AffineTransforms.scale(1, 1, 1);
    private static final Matrix4f DEFAULT_ROTATION = AffineTransforms.rotateY(1, 0);
    private static final Matrix4f DEFAULT_TRANSLATION = AffineTransforms.translate(0, 0, 0);
    private Matrix4f scale;
    private Matrix4f rotation;
    private Matrix4f translation;

    public Transformation(
            final Matrix4f scale,
            final Matrix4f rotation,
            final Matrix4f translation) {
        this.scale = scale;
        this.rotation = rotation;
        this.translation = translation;
    }
    public void setScale(Matrix4f scale){
        this.scale = scale;
    }
    public void setRotation(Matrix4f rotation){
        this.rotation = rotation;
    }
    public void setTranslation(Matrix4f translation){
        this.translation = translation;
    }
    public Matrix4f getScale(){
        return scale;
    }
    public Matrix4f getRotation(){
        return rotation;
    }
    public Matrix4f getTranslation(){
        return translation;
    }
    public Matrix4f getTransformation(){
        return translation.getMultiplicationWith(rotation).getMultiplicationWith(scale);
    }
    public static Transformation getDefaultTransformation() {
        return new Transformation(
                DEFAULT_SCALE,
                DEFAULT_ROTATION,
                DEFAULT_TRANSLATION
        );
    }
    public Transformation getWithDefaultScale() {
        return new Transformation(DEFAULT_SCALE, this.rotation, this.translation);
    }
    public Transformation getWithDefaultRotation() {
        return new Transformation(this.scale, DEFAULT_ROTATION, this.translation);
    }
    public Transformation getWithDefaultTranslation() {
        return new Transformation(this.scale, this.rotation, DEFAULT_TRANSLATION);
    }
    }