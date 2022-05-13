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

    Process_Info(int processID, int ArriveTime, int ServiceTime){
        this.processID = processID;
        this.ArriveTime = ArriveTime;
        this.ServiceTime = ServiceTime;
    }

    public int getProcessID() { return processID; };
    public int getArriveTime() { return ArriveTime; };
    public int getServiceTime() { return ServiceTime; };
    public int getExecTime() { return ExecTime; };


}

public class HRRN {
    public static void main(String[] args) {
        int [] process_id = {1,2,3,4,5,0};
        int [] arrive_time = {0,2,4,6,8};
        int [] service_time = {3,6,4,5,2};

        Queue<Process_Info> processQueue = new LinkedList<>();

        System.out.println("프로세스 입력 확인용 출력");
        System.out.println("----------------------------------------------------------------");
        for (int i=0; i<process_id.length; i++){
            if (process_id[i] < 0 || process_id[i] > 99) {
                System.out.println("범위 외의 Process ID가 있습니다");
                continue;
            }
            if (process_id[i] == 0){
                System.out.println("입력 종료");
                break;
            }
            else {
                Process_Info ps = new Process_Info(process_id[i], arrive_time[i], service_time[i]);

                System.out.printf("PID: %2d\t\t\t| 도착시간: %2d\t\t\t| Service Time: %2d",
                        ps.getProcessID(), ps.getArriveTime(), ps.getServiceTime());
                System.out.println();

                processQueue.add(ps);
                System.out.println("프로세스를 ReadyQueue에 삽입 완료");
                System.out.println();
            }
        }
        System.out.println("----------------------------------------------------------------");

        System.out.println("ReadyQueue에 있는 Process를 출력");
        System.out.println();

        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : processQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println("----------------------------------------------------------------");
    }

}
