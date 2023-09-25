package com.wxdgut.uilibrary.lockview.bean;

/**
 * id 表示该cell的编号，
 * centerX 表示该cell的圆心x坐标（相对坐标）
 * centerY 表示该cell的圆心y坐标（相对坐标）
 * centerX, centerY 圆心坐标如下：
 * (radius, radius)  (radius, 4radius)  (radius, 7radius)  (radius, 10radius)
 * (4radius, radius) (4radius, 4radius) (4radius, 7radius)  (4radius, 10radius)
 * (7radius, radius) (7radius, 4radius) (7radius, 7radius)  (7radius, 10radius)
 * (10radius, radius) (10radius, 4radius) (10radius, 7radius) (10radius, 10radius)
 * radius 表示该cell的半径
 * isHit 表示该cell是否被设置的标记
 *
 * {id=0, x=0, y=0, centerX=21.0, centerY=21.0, radius=21.0}
 * {id=1, x=0, y=1, centerX=21.0, centerY=84.0, radius=21.0}
 * {id=2, x=0, y=2, centerX=21.0, centerY=147.0, radius=21.0}
 * {id=3, x=0, y=3, centerX=21.0, centerY=210.0, radius=21.0}
 * {id=4, x=1, y=0, centerX=84.0, centerY=21.0, radius=21.0}
 * {id=5, x=1, y=1, centerX=84.0, centerY=84.0, radius=21.0}
 * {id=6, x=1, y=2, centerX=84.0, centerY=147.0, radius=21.0}
 * {id=7, x=1, y=3, centerX=84.0, centerY=210.0, radius=21.0}
 * {id=8, x=2, y=0, centerX=147.0, centerY=21.0, radius=21.0}
 * {id=9, x=2, y=1, centerX=147.0, centerY=84.0, radius=21.0}
 * {id=10, x=2, y=2, centerX=147.0, centerY=147.0, radius=21.0}
 * {id=11, x=2, y=3, centerX=147.0, centerY=210.0, radius=21.0}
 * {id=12, x=3, y=0, centerX=210.0, centerY=21.0, radius=21.0}
 * {id=13, x=3, y=1, centerX=210.0, centerY=84.0, radius=21.0}
 * {id=14, x=3, y=2, centerX=210.0, centerY=147.0, radius=21.0}
 * {id=15, x=3, y=3, centerX=210.0, centerY=210.0, radius=21.0}
 */
public class CellBean {
    private int id;
    private int x;
    private int y;
    private float centerX;
    private float centerY;
    private float radius;
    private boolean isHit;

    public CellBean(int id, int x, int y, float centerX, float centerY, float radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }

    /**
     * 是否触碰到该view
     */
    public boolean of(float x, float y) {
        float dx = this.centerX - x;
        float dy = this.centerY - y;
        // 中心点距离点击点的距离小于等于圆半径，表示点击到的是圆内
        double sqrt = Math.sqrt((dx * dx + dy * dy));
        return sqrt <= (double) this.radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    @Override
    public String toString() {
        return "CellBean{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", radius=" + radius +
                ", isHit=" + isHit +
                '}';
    }
}
