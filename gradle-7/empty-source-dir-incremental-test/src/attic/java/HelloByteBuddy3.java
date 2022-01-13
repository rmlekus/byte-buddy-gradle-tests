@net.bytebuddy.build.ToStringPlugin.Enhance
public class HelloByteBuddy3 {
    private String str = "Hello from "+this.getClass().getName()+"!";

    public static void main(String...args) {
        System.out.println(new HelloByteBuddy3().toString());
    }
}
