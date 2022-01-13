@net.bytebuddy.build.ToStringPlugin.Enhance
public class HelloByteBuddy1 {
    private String str = "Hello from "+this.getClass().getName()+"!";

    public static void main(String...args) {
        System.out.println(new HelloByteBuddy1().toString());
    }
}
