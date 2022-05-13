import java.util.*;

/*
<체크 해야하는 것들>
1) 서비스 시간을 항상 체크 -> 즉, 프로세스가 끝났는지 안끝났는지 확인을 주기적으로 해야 할 필요성이 있음
2) 우선순위 계산 -> R = (wait_time + service_time) / (service_time)
3) 대기 시간 -> curTime을 현재 시간이라고 하면, wait_time = (curTime - arrive_time)
4) 남은 서비스 시간 -> exec_Time을 현재까지 실행된 시간이라고 하면, (남은 서비스 시간) = (service_Time - exec_Time)
5) 현재 시간(curTime)
 */

/*
<< 프로세스 각각을 클래스의 객체로 다룸>>
# 클래스의 필드에는 프로세스의 정보가 들어감
  -> Process ID(프로세스 ID), Arrive Time(도착시간), Service Time(서비스 시간), Exec_Time(실행된 시간)
 */

class Process_Info {
    private int processID; // 프로세스 ID
    private int ArriveTime; // 해당 프로세스가 도착한 시간
    private int ServiceTime; // 해당 프로세스의 서비스 시간
    private int ExecTime; // 해당 프로세스가 현재까지 실행된 시간
    private int returnTime; // 해당 프로세스를 반환하기까지 걸린 시간(즉, 반환시간)

    Process_Info(int processID, int ArriveTime, int ServiceTime){
        this.processID = processID;
        this.ArriveTime = ArriveTime;
        this.ServiceTime = ServiceTime;
        ExecTime = 0;
        returnTime = 0;
    }

    public int getProcessID() { return processID; };
    public int getArriveTime() { return ArriveTime; };
    public int getServiceTime() { return ServiceTime; };
    public int getExecTime() { return ExecTime; };

    // 프로세스가 수행이 완료됬는지 확인하는 함수
    public boolean checkFinish() {
        if(ServiceTime == ExecTime) { // 서비스 시간과 현재까지 실행된 시간이 동일 -> 즉 모두 수행했음을 의미
            return true;
        }
        else{ // 수행 완료가 아닌 경우
            return false;
        }
    };

}

public class HRRN {

    public static Queue<Process_Info> readyQueue = new LinkedList<>();
    public int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수

    public static void main(String[] args) {
        //int [] process_id = {1,2,3,4,5,0};
        //int [] arrive_time = {0,2,4,6,8};
        //int [] service_time = {3,6,4,5,2};

        int [] process_id = {3,2,5,1,4,0};
        int [] arrive_time = {4,2,8,0,6};
        int [] service_time = {4,6,2,3,5};

        // int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수

        //Queue<Process_Info> processQueue = new LinkedList<>();
        // 프로세스 Input을 받아와 Arrive Time(도착 시간) 기준으로 정렬하여 큐에 저장 -> 우선순위 큐 이용


        PriorityQueue<Process_Info> processQueue = new PriorityQueue<>(new Comparator<Process_Info>() {
            @Override
            public int compare(Process_Info ps1, Process_Info ps2) {
                if(ps1.getArriveTime() < ps2.getArriveTime()) {
                    return -1;
                }
                else if(ps1.getArriveTime() > ps2.getArriveTime()) {
                    return 1;
                }
                else{
                    return 0;
                }
            }
        });


        int cnt = 0;

        System.out.println("프로세스 입력 확인용 출력");
        System.out.println("----------------------------------------------------------------");
        for (int i=0; i<process_id.length; i++){
            if (process_id[i] < 0 || process_id[i] > 99) {
                System.out.println("There is wrong Process ID (Out of PID's Range)");
                continue;
            }
            if (process_id[i] == 0){
                System.out.println("Stopping Input");
                System.out.println();
                break;
            }
            else {
                Process_Info ps = new Process_Info(process_id[i], arrive_time[i], service_time[i]);

                System.out.printf("Input the Process %d", i+1);
                System.out.println();

                processQueue.add(ps);
                cnt++;
                System.out.println("Add the Process into the Queue.");
                System.out.println();
            }
        }
        System.out.printf("Num of Process : %d\n", cnt);
        
        System.out.println("----------------------------------------------------------------");

        System.out.println("Sorting the Queue");

        System.out.println("Print the Process in the Queue");
        System.out.println();

        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : processQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println("----------------------------------------------------------------");

        Process_Info curPs = null;

        // 현재 시간(초)를 의미하는 변수
        // 스케줄링 시작하기 전을 의미하기 위해 -1로 세팅을 함
        int real_time = -1;


        // processQueue.poll();
        // System.out.println("큐 내용 삭제");

        while(true){
            ++real_time;
           if(processQueue.isEmpty()){
               break;
           }
           else {
               for(int i=0; i<processQueue.size(); i++){
                   Process_Info ps = processQueue.peek();
                   if(ps.getArriveTime() == real_time) {
                       readyQueue.offer(ps);
                       processQueue.poll();
                   }
               }
           }

        }


        //System.out.println("----------------------------------------------------------------");
        System.out.println("Ready Queue Information");
        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : readyQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println("----------------------------------------------------------------");

    }

    /*
    public void addQueue(Process_Info ps){
        boolean checkNewPs;
        checkNewPs = (ps != null);
        if(checkNewPs){
            num_Process += 1;
            readyQueue.add(ps);
        }
    }

    */
}

