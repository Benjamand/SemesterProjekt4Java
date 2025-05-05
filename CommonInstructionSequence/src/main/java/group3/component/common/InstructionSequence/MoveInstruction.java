package group3.component.common.InstructionSequence;

public class MoveInstruction extends Instruction {

     private Location location;

     public MoveInstruction(Location location) {
          this.location = location;
          this.action = ActionType.Move;
     }

     public Location getLocation() {
          return location;
     }

     public void setLocation(Location location) {
          this.location = location;
     }

     @Override
     public String toString() {
          return "Move to " + location; // Customize as needed
     }
}
