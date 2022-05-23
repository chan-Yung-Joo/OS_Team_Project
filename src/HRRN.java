import java.util.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public int ExecTime; // 해당 프로세스가 현재까지 실행된 시간
    public int returnTime; // 해당 프로세스를 반환하기까지 걸린 시간(즉, 반환시간)

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
    public void setExecTime(int ExecTime) { this.ExecTime = ExecTime; }

    // 프로세스가 수행이 완료됬는지 확인하는 함수
    public boolean checkFinish() {
        if(ServiceTime == ExecTime) { // 서비스 시간과 현재까지 실행된 시간이 동일 -> 즉 모두 수행했음을 의미
            return true;
        }
        else{ // 수행 완료가 아닌 경우
            return false;
        }
    };

    public double calc_waitTime(int curTime){
        return (curTime - ArriveTime);
    }

    public double calc_RatioTime(int curTime){
        return (double)((calc_waitTime(curTime) + ServiceTime) / (ServiceTime));
    }

}

public class HRRN {

    public static Queue<Process_Info> readyQueue = new LinkedList<>();
    //public int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수

    public static void main(String[] args) throws FileNotFoundException {

        List<String> input = new ArrayList<String>(); // 파일에서 데이터를 읽어올 공간(문자열로 읽기)
        List<Integer> arrange_input = new ArrayList<Integer>(); // 문자열로 읽은 데이터를 ','를 제거하고 저장하기 위한 공간
        Scanner scanner = new Scanner(new File("/Users/chanyungjoo/Desktop/School/2022-1학기/SW/OS_Team_Project/src/input.txt"));


        // 데이터를 줄 단위로 String 타입의 형태로 읽어와 저장
        while(scanner.hasNext()) {
            String str = scanner.next();
            input.add(str);
        }

        // 그 저장된 데이터들을 꺼내서 문자열 순회를 통해 ','를 제거하고 arrange_input에 저장
        for(String data : input) {
            for (int i = 0; i < data.length(); i++) {
                char obs = data.charAt(i);
                if (obs == ',') { continue; }
                else { arrange_input.add(Integer.parseInt(Character.toString(obs)));}
            }
        }

        List<Integer> process_id = new ArrayList<Integer>();
        List<Integer> arrive_time = new ArrayList<Integer>();
        List<Integer> service_time = new ArrayList<Integer>();

        /*
        현재 arrange_input에는 데이터가 다음과 같은 순서로 저장이 되어 있을 것임.
        >> [PID, Arrive_Time, Service_Time,PID, Arrive_Time, Service_Time,PID, Arrive_Time, Service_Time ....] <<
        PID, Arrive Time, Service Time을 분리해서 새로운 List에 저장하기 위해서 나머지 연산을 사용했음.
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 PID를 의미하고
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 Arrive Time을 의미하고
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 Service Time을 의미한다.
         */

        Input_Loop: for(int i=0; i<arrange_input.size(); i++) {
            if(i%3 == 0) {
                if (arrange_input.get(i) == 0) {
                    System.out.println("Process ID는 0이 불가합니다.");
                    break Input_Loop;
                }
                else if (arrange_input.get(i) < 0 || arrange_input.get(i) > 99) {
                    System.out.println("Out of Range (Process ID)");
                    break Input_Loop;
                }
                else { process_id.add(arrange_input.get(i)); }
            }

            else if(i%3 == 1){
                arrive_time.add(arrange_input.get(i));
            }
            else if(i%3 == 2) {
                service_time.add(arrange_input.get(i));
            }
        }

        int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수


        System.out.println("----------------------------------------------------------------");
        System.out.println("Input the Process");
        System.out.println("PID\t\tArrive Time\t\tService Time");
        for(int i=0; i< process_id.size(); i++){
            System.out.println(" " + process_id.get(i) + "\t\t\t" + arrive_time.get(i) +
                    "\t\t\t\t   " + service_time.get(i));
        }
        System.out.println("----------------------------------------------------------------");


        // 프로세스 Input을 받아와 Arrive Time(도착 시간) 기준으로 정렬하여 큐에 저장 -> 우선순위 큐 이용

        PriorityQueue<Process_Info> processQueue = new PriorityQueue<>(new Comparator<Process_Info>() {
            @Override
            public int compare(Process_Info ps1, Process_Info ps2) {
                if(ps1.getArriveTime() < ps2.getArriveTime()) { return -1; }
                else if(ps1.getArriveTime() > ps2.getArriveTime()) { return 1; }
                else { return 0; }
            }
        });


        System.out.println("----------------------------------------------------------------");
        for (int i=0; i<process_id.size(); i++){

            Process_Info ps = new Process_Info(process_id.get(i), arrive_time.get(i), service_time.get(i));

            System.out.printf("Input the Process %d", i+1);
            System.out.println();

            processQueue.add(ps);
            num_Process++;
            System.out.println("Add the Process into the Queue.");
            System.out.println();

        }
        System.out.printf("Num of Process : %d\n", num_Process);


        System.out.println("----------------------------------------------------------------");

        System.out.println("Print the Process in the Queue");
        System.out.println();

        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : processQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println("----------------------------------------------------------------");


        // 시간(초)를 의미하는 변수
        // 프로세스를 스케줄링 하기 전, ReadyQueue에 미리 넣기 위함 (사실은 비선점 모드라서 미리 가능한 일이라 생각이 듦)
        // 스케줄링 시작하기 전을 의미하기 위해 기본값으로 -1로 세팅을 함
        int time = -1;

        // Arrive Time 순으로 정렬된 프로세스들을 ReadyQueue로 넣어주는 작업
        // processQueue에서 peek 연산 후에 readyQueue에 대입 후 processQueue.poll을 통해 삭제
        // 최종적으로 readyQueue에 Process가 도착한 시간 순으로 정렬되어 저장되어 있고
        // processQueue는 poll 연산을 모두 진행했으므로 비어있는 상태가 될 것이다.


        while(true) {
            ++time;
           if(processQueue.isEmpty()){ break; }
           else {
               for(int i=0; i<processQueue.size(); i++){
                   Process_Info ps = processQueue.peek();
                   if(ps.getArriveTime() == time) {
                       readyQueue.offer(ps);
                       processQueue.poll();
                   }
               }
           }
        }



        /* System.out.println("Ready Queue Information");
        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : readyQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println("----------------------------------------------------------------");*/

        // 시간(초)를 의미하는 변수
        // 프로세스 스케줄링에 있어서 현재 시간이 필요함
        // 스케줄링 시작하기 전을 의미하기 위해 기본 값으로 -1로 세팅을 함
        int real_time = -1;

        //Process_Info curPs = readyQueue.peek();
        //System.out.printf("%d, %d, %d\n", curPs.getProcessID(), curPs.getArriveTime(), curPs.getServiceTime());

        Process_Info curPs = null;
        Process_Info nextPs = null;

        // 본격적으로 readyQueue에서 Process를 스케줄링을 진행할 부분

        while(num_Process!=0) {
            ++real_time;
            if(curPs != null) {
                ++curPs.ExecTime;

                for(var _process: readyQueue) { _process.calc_waitTime(real_time); }

                if(curPs.checkFinish()) {

                    System.out.printf(">>>> %d초에 수행 완료\t\t", real_time);
                    // 정규화된 반환 시간 = 반환시간/실행시간
                    System.out.printf("PID : %2d\tArrive Time: %2d\t,Service Time: %2d\tReturn Time: %2d\t\tNormalized Return Time: %.3f",
                            curPs.getProcessID(), curPs.getArriveTime(), curPs.getServiceTime(), real_time, (double)real_time/curPs.getServiceTime());
                    System.out.println();

                    readyQueue.remove(curPs);
                    --num_Process;
                    curPs = null;

                }

            }

            nextPs = readyQueue.peek();

            for (var obs : readyQueue) {
                if (obs.calc_RatioTime(real_time) > nextPs.calc_RatioTime(real_time)) {
                    nextPs = obs;
                }
            }
            curPs = nextPs;





            /*
            for (int i = 0; i < processQueue.size(); i++) {
                Process_Info ps = processQueue.peek();
                if (ps.getArriveTime() == real_time) {
                    readyQueue.offer(ps);
                    System.out.printf("Process %d 가 %d(초) 타이밍에 들어감\n", ps.getProcessID(), real_time);
                    processQueue.poll();
                }
            }

            //System.out.println(readyQueue.size());

            Process_Info curPs = readyQueue.peek();
            ++curPs.ExecTime;

            if (curPs.checkFinish()) { // 현재 프로세스가 수행을 완료했을 경우

                System.out.printf(">>>> %d초에 수행 완료\t\t", real_time);
                System.out.printf("PID : %2d\tArrive Time: %2d\t,Service Time: %2d\t,End Time: %2d\t",
                        curPs.getProcessID(), curPs.getArriveTime(), curPs.getServiceTime(), real_time);
                System.out.println();

                readyQueue.remove(curPs);
                num_Process--;

            }
            else {
                Process_Info nextPs = readyQueue.peek();


                //curPs.ExecTime++;
                for (var ps : readyQueue) {
                    if (ps.calc_RatioTime(real_time) > nextPs.calc_RatioTime(real_time)) {
                        nextPs = ps;
                    }
                }
                curPs = nextPs;
                //curPs.ExecTime++;
            } // else 문 */

        }

    } // void main 함수
} // class HRRN



