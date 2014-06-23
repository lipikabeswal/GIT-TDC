Overview of the drag and rotation implementation

1. FILES
  a) ControlHandle.lzx
  The view/component the user can interact with to execute actions like dragging,
 rotation, resizing.

  b) ControlView.lzx
  An invisible view sits on the canvas. All transformations to a manipulative are
  first executed on the ControlView, and then mirrored to the actual manipulative.

  c) DragMixin.lzx
  Replacement for the dragstate functionality in OpenLaszlo. The dragstate cannot
  deal with scaled views. If a component uses this mixin, it can be made draggable.
  Used by RefCard, MaskingRuler and ScratchPadWindow.

  d)DragRotateResize-Mixin.lzx
  Mixin with functionality for dragging, rotation and resizing of an OpenLaszlo
  view. The ControlView classes uses this mixin, since all the rotation and
  resizing is executed on the ControlView, and then the
  on(x,y,width,height,rotation) handlers on the ControlView are used to
  translate the ControlView position to a new position for the target
  manipulative.

  e) MouseBehavior-Mixin.lzx
  Mouse behavior mixing with support for two different types of clicks: Long and
  short. Decouples the click functionality from the normal OpenLaszlo click
  functionality, adding some flexibility.

  f) MovementLimiter-Mixin.lzx
  Limits the movement of a draggable view. Used in combination with the
  DragRotateResize-Mixin by Protractor and BaseRuler.

  g) Orientation.lzx
  Helper class for an orientation state (x, y, rotation) of an object in an
  x/y coordinate system.

  h) Point.lzx
  Point representation with helper function for calculations regarding rotation,
  distance between points, translating points, etc.

  i) ResizeMixin.lzx
  Like the DragMixin, a replacement for the resize state in OpenLaszlo, which
  does not support operation on scaled views.


2. ROTATION AND RESIZING
The rotation implementation uses an instance of the ControlView class to
position a view relative to its parent. The ControlView sits on the canvas
directly. When the user clicks one of the ControlHandles in the protractor
or ruler, the ControlView is resized and repositioned to exactly match the
location of the manipulative. The manipulative's position is relative to the upper
left corner (or origin of the view) of the gManipulatives instance.

When a mousemove event is sent, we need to calculate the position of the mouse
cursor relative to the center of the rotation to be able to calculate the
rotation angle as well as the distance between the mouse cursor and the rotation
center for the resizing. The following methods contain the relevant code:

  a) ControlHandler._trackMouseMove()
  Delegate is set up to call this method for global mousemove events. As long
  as the mouse button is down, will call DragRotateResize.doDragRotateResize()

  b) DragRotateResize.doDragRotateResize()
  Check if rotation or resizing is allowed for the manipulative. If this is the
  first call to the method during a rotate/resize action, calls
  _initializeRotation(). The actual rotation is done inside the _doRotation()
  method.
  For the resizing, the distance between the mouse cursor and rotation center is
  calculated.

  c) DragRotateResize._initializeRotation()
  Contains the code to start a rotation process. If the rotation center is not
  the upper left corner of a view, sets the xoffset and yoffset values to the x
  and y position of the rotation center. That is done for both the ControlView
  and the target manipulative.

  d) DragRotateResize._doRotation()
  Calculates the new rotation value to be applied to the manipulative, and uses
  the isValidLocation() method on the manipulative to check if the new position
  would be within the boundaries of the parent view (actually the logic is that
  any part of the grabber should never leave the gManipulatives view area).
  isValidLocation() calls the MovementLimiter.checkLocation() method, where
  the actual position check is done.

  e) MovementLimiter.checkLocation()
  The MovementLimiter mixin is used by the manipulatives, and the checkLocation()
  method uses a trick to simplify the calculation. An invisible view called
  "collisionmarker" is created in the oninit handler of the mixin. That view
  is then position inside the manipulative, overlaying the exact position of the
  grabber. The reason why the grabber position is not used directly is that the
  view structure for the ruler is so that the grabber is not a direct child of
  the BaseRuler, but is nested within it's view structure. Therefore adding
  the invisible collisionmarker, and positioning directly above the grabber
  keeps the validation code simpler.

  f) Global mouseup event / ControlHandle._doMouseUp() handler
  When the global mouseup event is fired during rotation/resize actions, the
  DragRotateResize.stopDragMove() method is called, and the mousemove and mouseup
  delegates are unregistered (inside the ControlHandle._doMouseUp()).

  g) DragRotateResize.stopDragMove()
  Calls DragRotateResize._resetAll(), which resets the state of the mixin and
  sets the xoffset/yoffset values of the manipulative back to 0, and applies the
  correct x/y and rotation values to the manipulative.

