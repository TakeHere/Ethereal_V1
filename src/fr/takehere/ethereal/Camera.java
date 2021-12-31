package fr.takehere.ethereal;

import fr.takehere.ethereal.objects.Pawn;
import fr.takehere.ethereal.utils.Vector2;

public class Camera {

    private Vector2 location = new Vector2(0,0);

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 newLocation) {
        for (Pawn pawn : Pawn.pawns) {
            pawn.location = pawn.location.add(this.location.subtract(newLocation));
        }

        this.location = newLocation;
    }
}
