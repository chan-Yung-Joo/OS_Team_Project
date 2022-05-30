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
6) 반환 시간(Return Time)
 */

/*
<< 프로세스 각각을 클래스의 객체로 다룸>>
# 클래스의 필드에는 프로세스의 정보가 들어감
  -> Process ID(프로세스 ID), Arrive Time(도착시간), Service Time(서비스 시간), Exec_Time(실행된 시간), Return Time(반환 시간)
     WaitTime(대기 시간), Left_Service_Time(남은 서비스 시간)
 */

class Process_Info {
    private int processID; // 프로세스 ID
    private int ArriveTime; // 해당 프로세스가 도착한 시간
    private int ServiceTime; // 해당 프로세스의 서비스 시간
    public int ExecTime; // 해당 프로세스가 현재까지 실행된 시간
    public int returnTime; // 해당 프로세스를 반환하기까지 걸린 시간(즉, 반환시간)
    public int waitTime; // 대기 시간
    public int left_service_time; // 남은 서비스 시간

    Process_Info(int processID, int ArriveTime, int ServiceTime){
        this.processID = processID;
        this.ArriveTime = ArriveTime;
        this.ServiceTime = ServiceTime;
        ExecTime = 0; returnTime = 0;
        waitTime = 0; left_service_time = ServiceTime;
    }

    public int getProcessID() { return this.processID; } // Process ID를 반환하는 메소드
    public int getArriveTime() { return this.ArriveTime; } // Process's Arrival Time을 반환하는 메소드
    public int getServiceTime() { return this.ServiceTime; } // Process's Service Time을 반환하는 메소드
    public int getReturnTime() { return this.returnTime; } // Process's Return Time을 반환하는 메소드
    public void setReturnTime(int curTime) { this.returnTime = curTime - this.ArriveTime; } // Process's Return Time을 설정하는 메소드


    // 프로세스가 수행이 완료됬는지 확인하는 메소드
    public boolean checkFinish() {
        if(this.ServiceTime == this.ExecTime) { // 서비스 시간과 현재까지 실행된 시간이 동일 -> 즉 모두 수행했음을 의미
            return true;
        }
        else{ // 수행 완료가 아닌 경우
            return false;
        }
    };

    public void calc_waitTime(int curTime){ // Process's Wait Time을 계산하는 메소드
        this.waitTime = curTime - this.ArriveTime;
    }

    public double calc_RatioTime(int curTime){ // Process의 응답비율을 계산하는 메소드
        return (double)((this.waitTime + left_service_time) / (left_service_time));
    }

}

public class HRRN {

    public static Queue<Process_Info> readyQueue = new LinkedList<>(); // LinkedList for ReadyQueue

    public static void main(String[] args) throws FileNotFoundException {

        List<String> input = new ArrayList<String>(); // 파일에서 데이터를 읽어올 공간(문자열로 읽기)
        List<String> arrange_input = new ArrayList<String>(); // 문자열로 읽은 데이터를 ','를 제거하고 저장하기 위한 공간
        Scanner scanner = new Scanner(new File("/Users/chanyungjoo/Desktop/School/2022-1학기/SW/OS_Team_Project/src/input.txt")); // 파일 입출력(Original Data)
        //Scanner scanner = new Scanner(new File("/Users/chanyungjoo/Desktop/School/2022-1학기/SW/OS_Team_Project/src/new_input.txt")); // 파일 입출력(New Sample Data)

        int count = 0;

        // 데이터를 줄 단위로 String 타입의 형태로 읽어와 저장
        while(scanner.hasNext()) {
            String str = scanner.next();
            input.add(str);
        }

        // 그 저장된 데이터들을 꺼내서 ','를 제거하고 arrange_input에 저장
        // 이때 나머지 연산을 이용하여 PID, Arrive Time, Service Time 순으로 저장
        for(String data : input) {
            String [] temp = data.split(",");
            for (int i = 0; i < temp.length; i++) {
                if (i%3 == 0) { arrange_input.add(temp[i]); }
                else if (i%3 == 1) { arrange_input.add(temp[i]); }
                else if (i%3 == 2) { arrange_input.add(temp[i]); }
            }
        }

        List<Integer> process_id = new ArrayList<Integer>(); // Integer List for Process ID
        List<Integer> arrive_time = new ArrayList<Integer>(); // Integer List for Process's Arrival Time
        List<Integer> service_time = new ArrayList<Integer>(); // Integer List for Process's Service TIme


        /*
        현재 arrange_input에는 데이터가 다음과 같은 순서로 저장이 되어 있을 것임.
        >> [PID, Arrive_Time, Service_Time,PID, Arrive_Time, Service_Time,PID, Arrive_Time, Service_Time ....] <<
        PID, Arrive Time, Service Time을 분리해서 새로운 List에 저장하기 위해서 나머지 연산을 사용했음.
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 PID를 의미하고
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 Arrive Time을 의미하고
        반복 변수(i)를 3으로 나누었을 때 나머지가 0이라는 것은 Service Time을 의미한다.
         */

        Input_Loop: for(int i=0; i<arrange_input.size(); i++) { // for 반복문에 Input_Loop이라는 Label을 지정
            if(i % 3 == 0) { // Process ID 인 경우
                if (Integer.parseInt(arrange_input.get(i)) == 0) { // Process ID가 0인 경우
                    System.out.println("Step 1) Process ID cannot be Zero. Exit Loop");
                    break Input_Loop; // 반복문 탈축
                }
                else if (Integer.parseInt(arrange_input.get(i)) < 0 || Integer.parseInt(arrange_input.get(i)) > 99) { // Process ID가 범위 외의 숫자이면
                    System.out.println("Step 1) Out of Range (Process ID). Exit Loop");
                    break Input_Loop; // 반복문 탈축
                }
                else { // Process ID가 정상적인 범위의 수이면
                    process_id.add(Integer.parseInt(arrange_input.get(i))); // Integer Type으로 변경하여 List에 추가
                }
            }

            else if(i % 3 == 1){ // Process's Arrival Time 인 경우
                arrive_time.add(Integer.parseInt(arrange_input.get(i))); // Integer Type으로 변경하여 List에 추가
            }
            else if(i % 3 == 2) { // Process's Service Time인 경우
                service_time.add(Integer.parseInt(arrange_input.get(i))); // Integer Type으로 변경하여 List에 추가
            }
        }

        int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수

        // 프로세스 Input을 받아와 Arrive Time(도착 시간) 기준으로 정렬하여 큐에 저장 -> 우선순위 큐 이용
        // 이 부분 코드는 쉽게 말해 도착 시간 순으로 Process 객체들을 정렬하는 부분을 담당하는 코드임
        PriorityQueue<Process_Info> processQueue = new PriorityQueue<>(new Comparator<Process_Info>() {
            @Override
            public int compare(Process_Info ps1, Process_Info ps2) {
                if(ps1.getArriveTime() < ps2.getArriveTime()) { return -1; }
                else if(ps1.getArriveTime() > ps2.getArriveTime()) { return 1; }
                else { return 0; }
            }
        });

        // 프로세스 객체를 생성해서 processQueue에 추가
        for (int i=0; i<process_id.size(); i++){

            Process_Info ps = new Process_Info(process_id.get(i), arrive_time.get(i), service_time.get(i));

            processQueue.add(ps);
            num_Process++;

        }
        System.out.println("Step 2) Add the Process into the Queue.");


        // processQueue에 추가된 프로세스들의 정보를 출력
        System.out.println("Step 3) Print the Process in the Queue");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (Process_Info process : processQueue){
            System.out.println(" " + process.getProcessID() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("<< HRRN Scheduling Algorithm's Result >>");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

        // 시간(초)를 의미하는 변수
        // 프로세스 스케줄링에 있어서 현재 시간이 필요함
        // 스케줄링 시작하기 전을 의미하기 위해 기본 값으로 -1로 세팅을 함
        int real_time = -1;


        Process_Info curPs = null; // 현재 Process를 위한 Class 변수
        Process_Info nextPs = null; // 다음 Process를 위한 Class 변수


        /*
        원래는 processQueue에서 하나씩 꺼내 오면서 도착 시간을 비교하여 readyQueue에 추가하려 했으나
        극단적인 예시로 4개의 프로세스가 0초에 동시에 들어오는 테스트를 하는 과정에서 그게 쉽지 않아서
        조금 더 편한 배열을 이용해서(여기서는 객체 배열) 동시에 들어오는 경우를 처리하기 위해서 번거롭지만 이렇게 하였음.
        장점으로는 이미 큐에서 도착 시간 별로 정렬을 하여 넣었기 때문에 정렬된 순서로 배열에 들어간다는 것.
         */

        int size = processQueue.size(); // 객체 배열 크기를 위한 변수
        int idx = 0; // 인덱스 변수

        Process_Info [] temp = new Process_Info[size]; // 객체 배열 선언


        Input_Loop: for(var _data : processQueue) { // 이 반복문을 Input_Loop이라는 Label을 지정
            if(idx == processQueue.size()) { // idx(인덱스 변수)가 processQueue의 크기와 같으면
                break Input_Loop; // Input_Loop 반복문을 탈출
            }
            temp[idx] = _data; // 현재 인덱스에 객체를 대입
            ++idx; // 인덱스 증가
            //idx++; 이렇게 해도 사실 같은 의미
        }


        // 본격적으로 readyQueue에서 Process를 스케줄링을 진행할 부분

        Loop : while(true) { // 이 반복문에 Loop 이라는 Label을 지정함

            if(num_Process == 0) { // 현재 프로세스의 개수가 0이면(즉, 모두 스케줄링이 완료했다면)
                break Loop; // Loop이라는 Label(반복문) 탈출하며 종료
            }

            ++real_time; // 시간 1 증가(선 증가, -1로 기본 세팅을 했기 때문)

            Process_Info _obs = null; // readyQueue에 삽입할때 이용할 Class 변수

            For_Loop: for(int i=0; i<temp.length; i++) { // 이 반복문을 For_Loop이라는 Label을 지정함
                _obs = temp[i]; // 객체 배열을 하나씩 순회하며 _obs 클래스 변수에 대입
                if(_obs.getArriveTime() == real_time) { // _obs 객체의 도착 시간과 현재 시간이 동일하면
                    readyQueue.add(_obs); // readyQueue에 추가
                }

            }


            if(curPs != null && !curPs.checkFinish()) { // 현재 실행되고 있는 프로세스가 있고 그 프로세스가 종료되지 않았다면
                ++curPs.ExecTime; // 현재 프로세스의 실행 시간 1 증가
                --curPs.left_service_time; // 현재 프로세스의 서비스 시간을 1 감소

            }

            if(curPs != null && curPs.checkFinish()) { // 현재 실행되는 프로세스가 있고 그 프로세스가 종료가 되었다면

                for (var _process : readyQueue) { // readyQueue의 프로세스들을 순회하며
                    _process.calc_waitTime(real_time); // 현재 시점에서 각 프로세스들의 대기 시간을 Update 시킴
                }

                System.out.printf("| %d초에 수행 완료\t\t", real_time);
                curPs.setReturnTime(real_time); // 수행 완료한 현재 프로세스의 반환 시간을 Update 시킴

                // 프로세스의 정보를 출력 (PID, Arrival Time, Service Time, Return Time, Normalized Return Time)
                // 정규화된 반환 시간 = 반환시간/실행시간
                System.out.printf("PID : %2d |\t\tArrive Time : %2d |\t\tService Time : %2d |\t\tReturn Time : %2d |\t\tNormalized Return Time : %.2f |",
                        curPs.getProcessID(), curPs.getArriveTime(), curPs.getServiceTime(), real_time, (double)curPs.getReturnTime() / curPs.getServiceTime());
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

                readyQueue.remove(curPs); // 수행 완료된 현재 프로세는 readyQueue에서 삭제
                --num_Process; // 수행해야 하는 프로세스의 개수를 1 감소
                curPs = null; // 현재 프로세스를 가리키는 Class 변수를 null로 초기화


            }

            nextPs = readyQueue.peek(); // 다음 프로세스를 readyQueue에서 peek() 연산을 통해 하나 가져옴


            for (var obs : readyQueue) { // readyQueue에 있는 모든 프로세스를 순회하면서
                
                // readyQueue에 있는 다른 프로세스와 nextPS 프로세스의 응답비율을 계산하여
                if (obs.calc_RatioTime(real_time) > nextPs.calc_RatioTime(real_time)) { // nextPs 프로세스 보다 큰 것이 있다면
                    nextPs = obs; // 해당하는 프로세스를 nextPs로 셋팅
                }
            }
            // 반복문을 통해 응답 비율이 가장 큰 프로세스를 찾았다면
            curPs = nextPs; // 현재 프로세스(curPs)를 nextPs로 변경


        } // while 문

    } // void main 함수
} // class HRRN



