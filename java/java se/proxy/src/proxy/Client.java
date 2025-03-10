package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Client {
	public static void main(String[] args){
		final ServerImpl source = new ServerImpl();
		Server proxy = (Server) Proxy.newProxyInstance(
				ServerImpl.class.getClassLoader(),	//目标类的类加载器
				ServerImpl.class.getInterfaces(),	//目标类实现的接口
				new InvocationHandler(){			//代理类实现的东西
					@Override
					public Object invoke(Object proxy, Method method,
								Object[] args) throws Throwable {
							if ("method".equals(method.getName())) {
								System.out.println("proxy method");
								method.invoke(source, args);
							}
							return null;
						}
				}
			);
		proxy.method();
		System.out.println("Done!");
	}
}
