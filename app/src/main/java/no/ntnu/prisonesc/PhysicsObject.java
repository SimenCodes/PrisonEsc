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
    private int rotation;//Et tall mellom 0 og 1800 hvor 0 er at han ser rett ned og 900 er at han ser rett fram og 180 er at han ser rett opp.
    private int gliderFactor;//Et tall for hvor god glideren er, den er 0 hvis det ikke er noen glider.
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
        this.velX += addGlider(true);
        this.velY += addGlider(false);
        if (accActive == 0) {
            accX = defaultAcc.x;
            accY = defaultAcc.y;
        } else {
            accActive--;
        }
        if (posY < 0) posY = 0; // Keep the player from going off the screen.
        if (posX < 0) posX = 0;
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

    /**
     * Skal fungere som en glider, vil gi en hastighet fremover proposjonal med hastigheten nedover.
     * Fremover og nedover er i forhold til rotasjonen til spilleren.
     * Returnerer et tall som skal legges til, ikke setter hastigheten.
     * Warning:Denne metoden bruker globale variable.(VelX, VelY, rotation og gliderFactor).
     * Denne vill føre til en oppbremsing hvis man går veldig skarpt oppover.
     * @param x true om det er x koordinaten vi legger til, false om det er y koordinaten.
     * @return
     */
    private int addGlider(boolean x) {
        int dirDown = rotation - 900;//Retningnen til ned for glideren.
        double dirSpeed = Math.toDegrees(Math.atan2(velY, velX)) * 10; //Retningen spilleren beveger seg i på samme format som orienteringen til spilleren.
        double fwdSpeed = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));//Hastigheten til spilleren i retningen den går i.
        double speedDown = Math.cos(dirSpeed - dirDown) * fwdSpeed;//Hvor mye av hastigheten til spilleren som går i rentning ned for glideren.
        double addSpeed = speedDown * gliderFactor;//Hvor mye som skal legges til i framoverretningen til spilleren.
        if (x)
            return (int) (Math.cos(rotation / 10.00 + 90) * addSpeed);//Hvor mye av addspeed som er i x retning
        else
            return (int) (Math.sin(rotation / 10.00 + 90 * addSpeed));//Hvor mye av addSpeed som er i y retning.
    }


    public Point getPos() {
        return new Point(posX, posY);
    }

    public void addVel(Point vel) {
        if (velX + vel.x >= 0) {
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

    public int getRot() {
        return this.rotation;
    }

    /**
     * Setter rotasjonen direkte
     *
     * @param rot
     */
    public void setRot(int rot) {
        this.rotation = rot;
    }

    public int getVelY() {
        return velY;
    }
}
