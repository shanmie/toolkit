package cn.org.toolkit.abstracts;

import cn.org.toolkit.A;

/**
 * @author deacon
 * @since 2019/5/31
 */
public abstract class AbsTest implements T {

    public void A(String v){
        B(v);
        System.out.println("a");
    }

    public void B(String v){
        System.out.println("b");
    }

    public abstract void C();
}
interface T{
    void A(String v);
}
class H extends AbsTest{

    @Override
    public void C() {
        System.out.println("ccc");
    }

    public void B(String v){
        System.out.println("c");
    }


    public static void main(String[] args) {
        H h = new H();
        h.A("这");
    }
}