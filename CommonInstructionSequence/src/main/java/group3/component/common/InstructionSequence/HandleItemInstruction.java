package group3.component.common.InstructionSequence;

public class HandleItemInstruction extends Instruction {

     private int itemId;

     private String item;

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
}
