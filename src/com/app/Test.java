package com.app;

public class Test {

	final int[] tc={0,0};
	public void test() {
		Thread[] tg = new Thread[2];
		Object o =new Object();
		int []slpcnf = {3,12};
		for(int i=0;i<tg.length;i++){
			tg[i] = buildThread(i,slpcnf[i]);
		}
		
		for(Thread t:tg){
			t.start();
		}
		int t=0;
		while(true){
			//System.out.println("main running");
			slp(1);
			for(int i=0;i<tc.length;i++){
				tc[i]++;
				//System.out.println("threshold:"+tc[i]);
				if(tc[i]>10){
					tg[i].stop();

					System.out.println(tg[i].getName()+" stopped instance="+tg[i].getId());
					tg[i]=buildThread(i, slpcnf[i]);
					tg[i].start();
					System.out.println(tg[i].getName()+" start instance="+tg[i].getId());
					tc[i]=0;
				}
			}
			

			
		}

	}

	private Thread buildThread(final int i,final int slp) {
		 Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						System.out.println(Thread.currentThread()+" running");
						tc[i]=0;
						slp(slp);
					}

				}
			}, "t"+i);
		return t;
	}

	public void slp(int sec) {
		try {
			Thread.sleep(1000 * sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Test t = new Test();
		t.test();
	}

}
