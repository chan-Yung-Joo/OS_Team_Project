import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class RoundRobin_이현동 {
   static class RR {
      int id, arrive, service;
      boolean run;
      
      RR(int id, int arrive, int service, boolean run) { // 
         this.id = id; // 프로세스 ID
         this.arrive = arrive; // 도착시간
         this.service = service; // 서비스 시간
         this.run = run; // 활성 상태 혹은 비활성상태(프로세스가 끝이 났거나, 아직 프로세스가 입력되지 않았을 때)
      }
   }
   
   public static void main(String[] args) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader("/Users/chanyungjoo/Desktop/School/2022-1학기/SW/OS_Team_Project/src/input.txt"));
      // BufferedReader br = new BufferedReader(new FileReader("/Users/chanyungjoo/Desktop/School/2022-1학기/SW/OS_Team_Project/src/new_input.txt"));

      int id, arrive, time;
      
      int[] first = new int[100]; // 남은시간
      int[] wait = new int[100]; // 대기시간
      
      int timeSlice = 4;
      
      LinkedList<RR> readyQueue = new LinkedList<>(); // 순차적으로 프로세스 담는 링크
      
      while(true) {
         String str = br.readLine();
         StringTokenizer st = new StringTokenizer(str,",");
         id = Integer.parseInt(st.nextToken());
         if(id == 0) {
            break;
         }
         arrive = Integer.parseInt(st.nextToken());
         time = Integer.parseInt(st.nextToken());
         
         readyQueue.add(new RR(id, arrive, time, false));
         first[id] = time;
         wait[id] = 0;
      }
      
      // 도착시간이 먼저인 순서대로 정렬
      readyQueue.sort(new Comparator<RR>() {

         @Override
         public int compare(RR o1, RR o2) {
            if(o1.arrive < o2.arrive) {
               return -1;
            } else {
               return 1;
            }
         }
      });
      
//      for(int i=0;i<readyQueue.size();i++) {
//         System.out.println(readyQueue.get(i).id);
//      }
      
      int T = 0; // 순차적으로 걸린 전체 시간
      int cnt = 0; // 프로세스 죽는 개수
      boolean cycle = false; // timeSlice == T 4초가 되었을 때 활성화
      int idx = 0; // 실행될 idx
      int temp = 0;
      
      while(cnt != readyQueue.size()) { // 프로세스가 다 죽을 경우 탈출
         for(int i = 0; i < readyQueue.size(); i++) {
            if(readyQueue.get(i).arrive == T) { // 전체시간이 프로세스 돌아가는 시간과 같다면 활성화
               readyQueue.get(i).run = true; // startQueue
            }
            if(readyQueue.get(i).run && cycle) { 
               if(readyQueue.get(i).service > 0 && wait[readyQueue.get(i).id] >= wait[readyQueue.get(idx).id]) {
                  idx = i;
               }
            }
         }
         
//         System.out.println("idx : " + idx);
         
         wait[readyQueue.get(idx).id] = 0;
         readyQueue.get(idx).service--;
         temp++;
         T++;
         
         for(int i = 0; i < readyQueue.size(); i++) {
            if(i != idx && readyQueue.get(i).run) {
               wait[readyQueue.get(i).id]++;
            }
         }
         
         if(readyQueue.get(idx).service > 0 && temp != timeSlice) {
            cycle = false;
         } else {
            if(readyQueue.get(idx).service == 0) {
               readyQueue.get(idx).run = false;
               cnt++;
               //System.out.printf("프로세스 ID : %d 도착시간 : %d 서비스 시간 : %d 종료 시간 : %d 반환 시간 : %d 정규화된 반환 시간 : %.3f\n", readyQueue.get(idx).id, readyQueue.get(idx).arrive, first[readyQueue.get(idx).id], T, T-readyQueue.get(idx).arrive, (double)(T-readyQueue.get(idx).arrive)/first[readyQueue.get(idx).id]);
               System.out.print("프로세스 ID : " + readyQueue.get(idx).id + ", ");
               System.out.print("도착시간 : " + readyQueue.get(idx).arrive + ", ");
               System.out.print("서비스 시간 : " + first[readyQueue.get(idx).id] + ", ");
               System.out.print("종료 시간 : " + T + ", ");
               System.out.print("반환 시간 : " + (T-readyQueue.get(idx).arrive) + ", ");
               System.out.print("정규화된 반환 시간 : " + (double)(T-readyQueue.get(idx).arrive)/first[readyQueue.get(idx).id] + " ");
               System.out.println();
            }
            temp = 0;
            cycle = true;
         }
         
      }
   }
}