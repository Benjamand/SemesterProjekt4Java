package group3.component.common.InstructionSequence;

public class HandleItemInstruction extends Instruction {
     // HandlingInstruction er instruktioner der involverer pick up og put down.

     private int itemId;

     private String item;

     public HandleItemInstruction(String item, int itemId, ActionType action) {
          this.item = item;
          this.itemId = itemId;
          this.action = action;
     }

     public int getItemId() {
          return itemId;
     }

     public void setItemId(int itemId) {
          this.itemId = itemId;
     }


     public String getItem() {
          return item;
     }

     public void setItem(String item) {
          this.item = item;
     }

     @Override
     public String toString() {
          return action + " item: " + item; // Customize as needed
     }
}
