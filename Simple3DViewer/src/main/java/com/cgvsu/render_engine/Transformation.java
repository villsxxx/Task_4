package com.cgvsu.render_engine;

import com.cgvsu.math.Matrix4f;

public class Transformation {
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



}
