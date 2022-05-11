import java.util.*;

/*
<체크 해야하는 것들>
1) 서비스 시간을 항상 체크 -> 즉, 프로세스가 끝났는지 안끝났는지 확인을 주기적으로 해야 할 필요성이 있음
2) 우선순위 계산 -> R = (wait_time + service_time) / (service_time)
3) 대기 시간
4) 남은 서비스 시간
5)
 */


public class HRRN {
    public static void main(String[] args) {
        int [] process_id = {1,2,3,4,5,0};
        int [] arrive_time = {0,2,4,6,8};
        int [] service_time = {3,6,4,5,2};

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
            System.out.printf("PID: %2d\t\t\t| 도착시간: %2d\t\t\t| Service Time: %2d",
                    process_id[i], arrive_time[i], service_time[i]);
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------");


    }

}
