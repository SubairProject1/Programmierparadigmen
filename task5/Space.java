public class Space<P> implements Approvable<P, Path<Space<P>>> {

    private String description;
    private ApprovalNode head;

    public Space(String description) {
        this.description = description;
    }

    @Override
    public Path<Space<P>> approved(P p) {
        ApprovalNode current = head;

        while (current != null) {
            if (current.criterion.equals(p)) {
                return current.path;
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public void approve(P p, Path<Space<P>> spaces) {
        ApprovalNode current = head;

        while(current != null) {
            if(current.criterion.equals(p)) {
                current.path = spaces;
                return;
            }
            current = current.next;
        }

        ApprovalNode newNode = new ApprovalNode(p, spaces);
        newNode.next = head;
        head = newNode; 
    }

    @Override
    public String toString() {
        return description;
    }

    private class ApprovalNode {
        P criterion;
        Path<Space<P>> path;
        ApprovalNode next;

        ApprovalNode(P criterion, Path<Space<P>> path) {
            this.criterion = criterion;
            this.path = path;
        }
    }
}
