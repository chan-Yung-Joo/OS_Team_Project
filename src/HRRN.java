import java.util.*;

public class HRRN {
    public static void main(String[] args) {
        int [] process_id = {1,2,3,4,5,0};
        int [] arrive_time = {0,2,4,6,8};
        int [] service_time = {3,6,4,5,2};

        for (int i=0; i<process_id.length; i++){
            if (process_id[i] == 0){
                System.out.println("입력 종료");
                break;
            }
            System.out.printf("PID: %2d\t\t\t| 도착시간: %2d\t\t\t| Service Time: %2d",
                    process_id[i], arrive_time[i], service_time[i]);
            System.out.println();
        }
    }

}
