package com.dg_livesports.dg_livesports;

import android.graphics.Bitmap;

/**
 * Created by DARLIN on 07/11/2016.
 */
public class Clasificaciones {

    private String pos;//posicion
    private String direction;//"u" subio en la clasificacion, "d" bajo en la clasificacion
    private String URLshield;
    private String team;
    private String points;
    private String round;//partidos jugados
    private String wins;
    private String draws;
    private String losses;
    private String gf;//goles fuera de casa
    private String ga;//goles en casa
    private String avg;//diferencia de goles
    public Bitmap shield;

    public Clasificaciones() {
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getDraws() {
        return draws;
    }

    public void setDraws(String draws) {
        this.draws = draws;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public String getGf() {
        return gf;
    }

    public void setGf(String gf) {
        this.gf = gf;
    }

    public String getGa() {
        return ga;
    }

    public void setGa(String ga) {
        this.ga = ga;
    }

    public String getURLshield() {
        return URLshield;
    }

    public void setURLshield(String URLshield) {
        this.URLshield = URLshield;
    }

    public void setShield(Bitmap shield) {
        this.shield = shield;
    }

    public Bitmap getShield() {
        return shield;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }
}
