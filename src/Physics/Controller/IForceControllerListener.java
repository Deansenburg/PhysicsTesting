package Physics.Controller;

import Shapes.ForceShape;

public interface IForceControllerListener {
    void onObjectAdded(ForceShape fs);
    void onObjectRemoved(ForceShape fs);
}
