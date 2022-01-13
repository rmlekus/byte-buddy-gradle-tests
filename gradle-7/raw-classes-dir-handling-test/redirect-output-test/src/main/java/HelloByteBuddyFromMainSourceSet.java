@net.bytebuddy.build.ToStringPlugin.Enhance
public class HelloByteBuddyFromMainSourceSet {
    private String str = "Hello from "+this.getClass().getName()+"!";

    public static void main(String...args) {
        System.out.println(new HelloByteBuddyFromMainSourceSet().toString());
    }
}
