/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Date;

/**
 *
 * @author pere
 */
public class Missatge {
    private String missage;
    private String emisor;
    private Date date;

    public Missatge(String missage, String emisor, long date) {
        this.missage = missage;

        this.emisor = emisor;
        this.date = new Date(date * 1000);
    }

    public Missatge() {
        this.missage = null;

        this.emisor = null;
        this.date = null;
    }

    public String getMissage() {
        return missage;
    }


    public String getEmisor() {
        return emisor;
    }

    public Date getDate() {
        return date;
    }
    
    
}
