import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Random;

public class shortestPath{

    static int n = 1000;
    static int[][] a = new int[n][n];
    public static void init() {
        System.out.println("规模为:" + n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = Integer.MAX_VALUE >> 1;
            }
            a[i][i] = 0;
        }

        double sparsity = 0.5;//控制稀疏度
        System.out.println("稀疏度为:" + sparsity);
        Random random=new Random();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.random() <= sparsity) {   
                    a[i][j] = random.nextInt(201) - 100;
                }
            }
        }
        //避免独立点的存在，随机找点连接
        for (int j = 1; j < n; j++) {
            boolean flag = false;
            for(int i = 0; i < j; i++){
                if(a[i][j] != Integer.MAX_VALUE >> 1){
                    flag = true;
                    break;
                }
            }
            if(flag == false){
                int index_i = random.nextInt(j);
                a[index_i][j] = random.nextInt(201) - 100;
            }
        }
    }   

    // private static void floyd() {
    //     for (int k = 0; k < n; k++) {
    //         for (int i = 0; i < n; i++) {
    //             for (int j = 0; j < n; j++) {
    //                 a[i][j] = Math.min(a[i][j], a[i][k] + a[k][j]);
    //             }
    //         }
    //     }
    //     for (int i = 0; i < n; i++) {
    //         for (int j = i + 1; j < n; j++) {
    //             System.out.println(i + " " + j + ":" + a[i][j]);
    //         }
    //     }
    // }

    public static void dijkstra(int p) {
        int[] d = new int[n];
        Set<Integer> set = new HashSet<>(n);
        set.add(p);
        for (int i = 0; i < n; i++) {
            d[i] = a[p][i];
        }
        while (set.size() < n) {
            int min_dis = Integer.MAX_VALUE;
            int num = 0;
            for (int i = 0; i < n; i++) {
                if (!set.contains(i) && min_dis > d[i]) {
                    min_dis = d[i];
                    num = i;
                }
            }
            for (int i = 0; i < n; i++) {
                if (!set.contains(i)) {
                    d[i] = Math.min(d[i], d[num] + a[num][i]);
                }
            }
            set.add(num);
        }
        // for (int i = 0; i < n; i++) {
        //     System.out.println("点" + p + "到点" + i + "的距离为：" + d[i]);
        // }
    }

    public static boolean bellmanFord(int s){
        int[] d = new int[n];
        for(int i=0;i<n;i++){
            d[i] = a[s][i];
        }
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n;j++){
                for(int k=0;k<n;k++){
                    if(a[j][k]!=Integer.MAX_VALUE){
                        d[k] = Math.min(d[k],d[j]+a[j][k]);
                    }
                }
            }
        }
        //检测负环
        for(int j=0;j<n;j++) {
            for (int k = 0; k < n; k++) {
                if (a[j][k] != Integer.MAX_VALUE) {
                    if(d[k]>d[j]+a[j][k]){
                        System.out.println("negative weight ring");
                        return false;
                    }
                }
            }
        }
        // for (int i = s + 1; i < n; i++) {
        //     System.out.println("点" + s + "到点" + i + "的距离为：" + d[i]);
        // }
        return true;
    }
    

    public static boolean spfa(int s){
        Queue<Integer> queue = new ArrayDeque<>(n);
        int[] d = new int[n];
        int[] num = new int[n]; //记录入队次数，用于检测负环
        num[s] = 1;
        boolean[] flag = new boolean[n];
        for(int i=0;i<n;i++){
            d[i] = Integer.MAX_VALUE;
            flag[i] = false;
        }
        d[s] = 0;
        queue.add(s);
        flag[s] = true;
        Integer u;
        while ((u=queue.poll())!=null){
            flag[u] = false;
            for(int i=0;i<n;i++){
                if(a[u][i]!=Integer.MAX_VALUE>>1){
                    int temp = d[u] + a[u][i];
                    if(temp<d[i]){
                        d[i] = temp;
                        if(!flag[i]){
                            queue.add(i);
                            num[i]++;
                            if(num[i] > n){
                                System.out.println("negative weight ring");
                                return false;
                            }
                            flag[i] = true;
                        }
                    }
                }
            }
        }
        // for (int i = s + 1; i < n; i++) {
        //     System.out.println("点" + s + "到点" + i + "的距离为：" + d[i]);
        // }
        return true;
    }

    public  static void main(String[] args) {
        init();
        // floyd();
        long startTime=System.currentTimeMillis();
        dijkstra(0);
        long dijkstra_end=System.currentTimeMillis();
        System.out.println("dijkstra运行时间： "+(dijkstra_end-startTime)+"ms");  

        spfa(0);
        long spfa_time =System.currentTimeMillis();
        System.out.println("spfa运行时间： "+(spfa_time-dijkstra_end)+"ms");  

        bellmanFord(0);
        long endTime=System.currentTimeMillis();
        System.out.println("bellmanFord运行时间： "+(endTime-spfa_time)+"ms");  
        System.out.println("程序总运行时间： "+(endTime-startTime)+"ms");  
    }
         
}


