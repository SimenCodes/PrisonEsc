package no.ntnu.prisonesc;

import android.util.Log;

/**
 * Created by Henrik on 02.04.2017.
 */

public class PhysicsObject {
    private static final String TAG = "PhysicsObject";

    private static final float GROUND_BOUNCE = 0.0f; // DISABLE: Sett til 0 for å deaktivere bounce

    private int accX;
    private int accY;
    private Point defaultAcc;
    private int velX;
    private int velY;
    private int posX;
    private int posY;
    private OldRotation rotation;//Et tall mellom 0 og 1800 hvor 0 er at han ser rett ned og 900 er at han ser rett fram og 180 er at han ser rett opp.
    private double gliderFactor;//Et tall for hvor god glideren er, den er 0 hvis det ikke er noen glider.
    private double drag;//Denne er høy hvis hvis spilleren er lite aerodynamisk og lav hvis spilleren er veldig aerodynamisk.
    //drag må være ganske liten <0.2


    private int accActive; // hvor mange ticks det er igjen av akslerasjonen.


    public PhysicsObject(double drag, double gliderFacotor, int posY, int velX, int velY, int accY) {
        this.accX = 0;
        this.accY = accY;
        this.defaultAcc = new Point(0, accY);
        this.velX = velX;
        this.velY = velY;
        this.posX = 0;
        this.posY = posY;
        this.drag = drag;
        this.gliderFactor = gliderFacotor;
        this.accActive = 0;
        this.rotation = new OldRotation();
    }

    public void tick() {
        //Vær oppmerksom på at rekkefølgen her har mye å si.
        //DISABLE: Her kan man skru av enkeltaspekter ved fysikken
        int tempVelX = velX;//For at ikke rekkefølge skal ha like mye å si
        int tempVelY = velY;//Kan være hensiktsmessig å gjøre det samme for posisjon og akslerasjon også på et senere punkt.
        this.posX += this.velX;
        this.posY += this.velY;
        tempVelX += this.accX;//DISABLE: Ved å komentere ut disse linjene skrur man av gravitasjonen
        tempVelY += this.accY;//DISABLE: Ved å komentere ut disse linjene skrur man av gravitasjonen
        tempVelX += addDrag(true);//DISABLE: Ved å komentere ut disse lijene skrur man av drag
        tempVelY += addDrag(false);//DISABLE: Ved å komentere ut disse lijene skrur man av drag;
        tempVelX += addGlider2(true);//DISABLE: Ved å komentere ut disse lijene skrur man av glider
        tempVelY += addGlider2(false);//DISABLE: Ved å komentere ut disse lijene skrur man av glider
        this.velX = tempVelX;
        this.velY = tempVelY;
        if (accActive == 0) {//DISABLE: Ved å sette denne til false skrur man av raketter.
            accX = defaultAcc.x;
            accY = defaultAcc.y;
        } else {
            accActive--;
        }
        if (posY < 0) {
            posY = 0; // Keep the player from going off the screen.
            if (velY < 0) velY = (int) (-velY * GROUND_BOUNCE); // Bounce
            velX = (int) (velX * GROUND_BOUNCE);
        }
        if (posX < 0) {
            posX = 0;
            Log.w(TAG, "tick: Almost went through through ground X");
        }
        if (velX <= 0) {
            velX = 0;
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

        //Log.d(TAG, "addDrag.scaledSpeed, velX, velY: " + scaledSpeed+" , " + velX + " , " + velY);
        //Bruker formlike trekanter her
        if (x) {
            return (int) ((velX * scaledSpeed) / fwdSpeed);
        } else {
            return (int) ((velY * scaledSpeed) / fwdSpeed);
        }
    }

    /**
     * Skal fungere som en glider, vil gi en hastighet fremover proposjonal med hastigheten nedover.
     * I tillegg kan den gi en hastighet oppover i for å gi en illusjon av å gli.
     * Fremover og nedover er i forhold til rotasjonen til spilleren.
     * Returnerer et tall som skal legges til, ikke setter hastigheten.
     * Warning:Denne metoden bruker globale variable.(VelX, VelY, rotation og gliderFactor).
     * Denne vill føre til en oppbremsing hvis man går veldig skarpt oppover.
     * Denne skal optimalt ikke påvirke noe når man beveger seg rett i bevegelsesretningen.
     *
     * @param x true om det er x koordinaten vi legger til, false om det er y koordinaten.
     * @return The value to add to the current velocity.
     */
    public int addGlider(boolean x) {
        OldRotation dirDown = rotation.rotated(-900);//Retningnen til ned for glideren. V:Minus pga funksjonen til Atan2
        OldRotation dirSpeed = new OldRotation((float) Math.atan2(velY, velX)); //Retningen spilleren beveger seg i på samme format som orienteringen til spilleren.
        //Log.d(TAG, "addGlider.dirSpeed: " + dirSpeed);
        double fwdSpeed = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));//Hastigheten til spilleren i retningen den går i.
        double speedDown = Math.cos(dirSpeed.subtracted(dirDown).getRad()) * fwdSpeed;//Hvor mye av hastigheten til spilleren som går i rentning ned for glideren.
        double addSpeed = speedDown * gliderFactor;//Hvor mye som skal legges til i framoverretningen til spilleren.
        if (x)
            return (int) (Math.cos(rotation.rotated((float) Math.PI / 4).getRad()) * addSpeed);//Hvor mye av addspeed som er i x retning
        else
            return (int) (Math.sin(rotation.rotated((float) Math.PI / 4).getRad()) * addSpeed);//Hvor mye av addSpeed som er i y retning.
    }

    /**
     * En annen tillnærming til glider.
     *
     * @param x
     * @return
     */
    public int addGlider2(boolean x) {
        OldRotation dirDown = rotation.rotated((float) (-Math.PI / 2));//Retningnen til ned for glideren. V:Minus pga funksjonen til Atan2
        OldRotation dirSpeed = new OldRotation((float) Math.atan2(velY, velX)); //Retningen spilleren beveger seg i på samme format som orienteringen til spilleren.
        //Log.d(TAG, "addGlider.dirSpeed: " + dirSpeed);
        double fwdSpeed = Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));//Hastigheten til spilleren i retningen den går i.
        double speedDown = Math.cos(dirSpeed.subtracted(dirDown).getRad()) * fwdSpeed;//Hvor mye av hastigheten til spilleren som går i rentning ned for glideren.
        double addSpeed = - speedDown * gliderFactor;//Hvor mye som skal legges til i oppoverretningen for spilleren.
        //Log.d(TAG,"addGlider2.dirDown: "+dirDown.getDeg()+ "addGlider2.addSpeed: "+addSpeed+" addglider2.fwdSpeed: "+ fwdSpeed);
        if (x)
            return (int) (Math.cos(dirDown.getRad()) * addSpeed);//Hvor mye av addspeed som er i x retning
        else
            return (int) (Math.sin(dirDown.getRad()) * addSpeed);//Hvor mye av addSpeed som er i y retning.
    }


    public Point getPos() {
        return new Point(posX, posY);
    }

    public void addVel(Point vel) {
        this.velX += vel.x;
        this.velY += vel.y;
        if (this.velX < 0) {
            this.velX = 0; // Vi rydder stille opp her
            Log.w(TAG, "addVel: You tried moving backwards. That's not allowed, so now your velX is 0");
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

    public OldRotation getRot() {
        return this.rotation;
    }

    /**
     * Setter rotasjonen direkte
     *
     * @param rot
     */
    public void setRot(OldRotation rot) {
        this.rotation = rot;
        Log.d(TAG, "setRot.rot: "+rot.getDeg());
    }

    public int getVelY() {
        return velY;
    }

    public void reduseAirResistance(double amount) {
        this.drag -= amount;
    }
}
