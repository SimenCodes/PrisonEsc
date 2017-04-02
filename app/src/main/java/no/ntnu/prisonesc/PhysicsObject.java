package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

public class PhysicsObject {
    private int accX;
    private int accY;
    private Point defaultAcc;
    private int velX;
    private int velY;
    private int posX;
    private int posY;
    private int rotation;//Et tall mellom 0 og 180 hvor 0 er at han ser rett ned og 90 er at han ser rett fram og 180 er at han ser rett opp.
    private double drag;//Denne er høy hvis player har høy hvis spilleren er lite aerodynamisk og lav hvis spilleren er veldig aerodynamis.
    //drag må være ganske liten <0.2

    private int accActive; // hvor mange ticks det er igjen av akslerasjonen.


    public PhysicsObject(double drag, int posY, int velX, int velY, int accY) {
        this.accX = 0;
        this.accY = accY;
        this.defaultAcc = new Point(0, accY);
        this.velX = velX;
        this.velY = velY;
        this.posX = 0;
        this.posY = posY;
        this.drag = drag;
        this.accActive = 0;
        this.rotation = (int) Math.toDegrees(Math.atan2((double) velY, (double) velX));
    }

    public void tick() {
        //Vær oppmerksom på at rekkefølgen her har mye å si.
        this.posX += this.velX;
        this.posY += this.velY;
        this.velX += this.accX;
        this.velY += this.accY;
        this.velX += addDrag(true);
        this.velY += addDrag(false);
        if (accActive == 0) {
            accX = defaultAcc.x;
            accY = defaultAcc.y;
        } else {
            accActive--;
        }
    }

    /**
     * Intensjonen her er at denne funksjonen gir ut et tall som kann legges til farten for å simulere luftmotstand.
     * Warning: Denne funksjonen bruker globale variable velX og velY.
     *
     * @param x true hvis vi vil ha x koordinaten, false hvis vi vil ha y koordinaten.
     * @return
     */
    private int addDrag(boolean x) {
        double fwdSpeed = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));
        //minus fordi den skal gå baklengs, fwdSpeed^2 for å få en polomyal funksjon og skalerer det med drag.
        double scaledSpeed = -Math.pow(fwdSpeed, 2) * drag;
        //Bruker formlike trekanter her:
        if (x) {
            return (int) ((velX * scaledSpeed) / fwdSpeed);
        } else {
            return (int) ((velY * scaledSpeed) / fwdSpeed);
        }
    }

    public Point getPos() {
        return new Point(posX, posY);
    }

    public void addVel(Point vel) {
        if (velX + vel.x > 0) {
            this.velX += vel.x;
            this.velY += vel.y;
        } else {
            throw new IllegalArgumentException("Du kan ikke gå baklengs");
        }
    }

    /**
     * @param value    hvor mye aklserasjonen skal settes til
     * @param duration hvor lenge akslerasjonen er aktiv. (Antall ticks).
     * @return false hvis en akslerasjon allerede er aktivert, true hvis du bruker denne
     */
    public boolean setAcc(Point value, int duration) {
        if (accActive == 0) {
            accActive = duration;
            this.accX = value.x;
            this.accY = value.y;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter rotasjonen direkte
     *
     * @param rot
     */
    public void setRot(int rot) {
        this.rotation = rot;
    }
}
