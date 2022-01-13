@net.bytebuddy.build.ToStringPlugin.Enhance
public class HelloByteBuddyFromTestSourceSet {
    private String str = "Hello from "+this.getClass().getName()+"!";

    public static void main(String...args) {
        System.out.println(new HelloByteBuddyFromTestSourceSet().toString());
    }
}
