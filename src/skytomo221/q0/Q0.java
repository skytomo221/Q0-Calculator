package skytomo221.q0;

class Q0 {
    public static void main(String args[]) {
        Q0Controller controller = new Q0Controller();
        Q0Viewer viewer = new Q0Viewer(controller);
        controller.setViewer(viewer);
        viewer.setVisible(true);
    }
}
