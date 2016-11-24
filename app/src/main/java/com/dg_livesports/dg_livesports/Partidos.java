package com.dg_livesports.dg_livesports;

import android.graphics.Bitmap;

/**
 * Created by DARLIN on 08/11/2016.
 */
public class Partidos {

    private String local;
    private String visitor;
    private String competition_name;
    private String URLcflag;
    private String URLlocal_shield;
    private String URLvisitor_shield;
    private String date;
    private String result;
    private String extraTxt;// tiempo que falta o que se jugo un partido
    private String hour;// hora del partido
    private String minute;
    private String live_minute;// minuto en vivo
    public Bitmap local_shield;
    public Bitmap visitor_shield;
    public Bitmap cflag;

    public Partidos() {}

    public String getLive_minute() {return live_minute;}

    public void setLive_minute(String live_minute) {this.live_minute = live_minute;}

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getCompetition_name() {
        return competition_name;
    }

    public void setCompetition_name(String competition_name) {
        this.competition_name = competition_name;
    }

    public String getURLcflag() {
        return URLcflag;
    }

    public void setURLcflag(String URLcflag) {
        this.URLcflag = URLcflag;
    }

    public String getURLlocal_shield() {
        return URLlocal_shield;
    }

    public void setURLlocal_shield(String URLlocal_shield) {
        this.URLlocal_shield = URLlocal_shield;
    }

    public String getURLvisitor_shield() {
        return URLvisitor_shield;
    }

    public void setURLvisitor_shield(String URLvisitor_shield) {
        this.URLvisitor_shield = URLvisitor_shield;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getExtraTxt() {
        return extraTxt;
    }

    public void setExtraTxt(String extraTxt) {
        this.extraTxt = extraTxt;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public Bitmap getLocal_shield() {
        return local_shield;
    }

    public void setLocal_shield(Bitmap local_shield) {
        this.local_shield = local_shield;
    }

    public Bitmap getVisitor_shield() {
        return visitor_shield;
    }

    public void setVisitor_shield(Bitmap visitor_shield) {
        this.visitor_shield = visitor_shield;
    }

    public Bitmap getCflag() {
        return cflag;
    }

    public void setCflag(Bitmap cflag) {
        this.cflag = cflag;
    }
}
