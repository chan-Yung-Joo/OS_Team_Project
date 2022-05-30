import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

//프로세스 ID, 도착시간, 서비스 시간, 종료시간, 반환시간, 정규화된 반환시간
/*
	1) 프로세스 ID (processId)
	2) 도착 시간(arriveTime) 
	3) 서비스 시간(serviceTime)
	4) 종료 시간(endTime)
	5) 반환 시간(returnTime) = 대기 시간(waitTime) + 실행 시간(execTime)
	6) 정규화된 반환시간(Normalized_Return_Time) = 반환시간(returnTime)/서비스 시간
	etc) 대기시간(waitTime), 실행시간(execTime), 남은 서비스 시간(leftServiceTime)
 */
class processInfo{
	private int processId;
	private int arriveTime;
	private int serviceTime;
	private int endTime;
	public int execTime;
	public int returnTime;
	public int waitTime;
	public int leftServiceTime;
	
	processInfo(int processId, int arriveTime, int serviceTime){
		this.processId = processId;
		this.arriveTime = arriveTime;
		this.serviceTime = serviceTime;
		execTime = 0; returnTime = 0;
		waitTime = 0; leftServiceTime = 0;
	}
	public int getProcessId() {return this.processId;} // 프로세스 Id(processId)를 반환
	public int getArriveTime() {return this.arriveTime;} // 도착시간(arriveTime)을 반환
	public int getServiceTime() {return this.serviceTime;} // 서비스 시간(serviceTime)을 반환
	public int getReturnTime() {return this.returnTime;} // 반환 시간(returnTime)을 반환
	public void setReturnTime(int curTime) {this.returnTime=curTime-this.arriveTime;} // 반환시간(returnTime)을 설정
	
	// 프로세스의 실행이 끝났는지 확인하는 메소드
	public boolean checkReturn() {
		if(this.serviceTime == this.execTime) {
			return true;
		}
		else {
			return false;
		}
	};
	// 대기 시간(waitTime) 계산
	public void calc_waitTime(int curTime) {
		this.waitTime = curTime - this.arriveTime;
	}
}
public class MFQ {
	public static Queue<processInfo> readyQueue0 = new LinkedList<>();
	public static Queue<processInfo> readyQueue1 = new LinkedList<>();
	public static Queue<processInfo> readyQueue2 = new LinkedList<>();
	public static Queue<processInfo> readyQueue3 = new LinkedList<>();
	
	public static void main(String[] args) throws IOException {
		List<String> input = new ArrayList<String>(); // 파일에서 데이터를 읽어올 공간(문자열로 읽기)
		List<String> input_num = new ArrayList<String>(); // 문자열로 읽은 데이터를 ','를 제거하고 저장하기 위한 공간
		Scanner scan = new Scanner(new File("C:\\Users\\ju003\\OneDrive\\바탕 화면\\input.txt")); // 파일 입출력(Original Data)
		
		int[] q = {1,2,4,8};
		
		while(scan.hasNext()) {
			String str = scan.next();
			input.add(str);
		}
		
		for(String data: input) {
			String[] temp = data.split(",");
			for(int i=0;i<temp.length;i++) {
				if(i%3==0) // 프로세스 ID (processId)
					input_num.add(temp[i]);
				else if(i%3==1) // 도착 시간(arriveTime) 
					input_num.add(temp[i]);
				else if(i%3==2) // 서비스 시간(serviceTime)
					input_num.add(temp[i]);
			}
		}
		List<Integer> process_id = new ArrayList<Integer>(); // 프로세스 Id 리스트
		List<Integer> arrive_time = new ArrayList<Integer>(); // 도착 시간 리스트
		List<Integer> service_time = new ArrayList<Integer>(); // 서비스 시간 리스트
		
		for(int i=0;i<input_num.size();i++) {
			if(i%3<=0) { // 프로세스 ID (processId)
				if(Integer.parseInt(input_num.get(i))==0) {
					break;
				}
				// processId가 0보다 큰 경우에는 process_id라는 프로세스 Id 리스트에 넣는다.
				process_id.add(Integer.parseInt(input_num.get(i)));
			}
			else if(i%3 == 1) { // 도착 시간(arriveTime) 
				arrive_time.add(Integer.parseInt(input_num.get(i)));
			}
			else if(i%3 == 2) { // 서비스 시간(serviceTime)
				service_time.add(Integer.parseInt(input_num.get(i)));
			}
		}
		int num_Process = 0; // 입력된 프로세스의 개수를 저장하는 변수
		
		// 프로세스 Input을 받아와 Arrive Time(도착 시간) 기준으로 정렬하여 큐에 저장 -> 우선순위 큐 이용
        // 이 부분 코드는 쉽게 말해 도착 시간 순으로 Process 객체들을 정렬하는 부분을 담당하는 코드임
        PriorityQueue<processInfo> processQueue = new PriorityQueue<>(new Comparator<processInfo>() {
            @Override
            public int compare(processInfo ps1, processInfo ps2) {
                if(ps1.getArriveTime() < ps2.getArriveTime()) { return -1; }
                else if(ps1.getArriveTime() > ps2.getArriveTime()) { return 1; }
                else { return 0; }
            }
        });
        
        // 프로세스 객체를 생성해서 processQueue에 추가
        for (int i=0; i<process_id.size(); i++){

        	processInfo ps = new processInfo(process_id.get(i), arrive_time.get(i), service_time.get(i));

            processQueue.add(ps);
            num_Process++;
        }
        
     // processQueue에 추가된 프로세스들의 정보를 출력
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("PID\t\tArrive Time\t\tService Time");
        for (processInfo process : processQueue){
            System.out.println(" " + process.getProcessId() + "\t\t\t" + process.getArriveTime() +
                    "\t\t\t\t   " + process.getServiceTime());
        }
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("<< MFQ Scheduling Algorithm's Result >>");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

        // 시간(초)를 의미하는 변수
        // 프로세스 스케줄링에 있어서 현재 시간이 필요함
        // 스케줄링 시작하기 전을 의미하기 위해 기본 값으로 -1로 세팅을 함
        int real_time = -1;
        
        processInfo curPs = null; // 현재 Process를 위한 Class 변수
        processInfo nextPs = null; // 다음 Process를 위한 Class 변수
        
        int size = processQueue.size();
        int idx=0;
        
        processInfo[] temp = new processInfo[size];
        
        for(var _data : processQueue) {
        	if(idx == processQueue.size())
        		break;
        	temp[idx] = _data;
        	idx++;
        }
        int j=0;
        while(true) {
        	if(num_Process == 0) // 현재 프로세스의 개수가 0이면(즉, 모두 스케줄링이 완료했다면)
        		break;
        	real_time++; // 시간 1 증가(선 증가, -1로 기본 세팅을 했기 때문)
        	
        	processInfo obs = null; // readyQueue에 삽입할때 이용할 Class 변수
        	
        	for(int i =0;i<temp.length;i++) {
        		obs=temp[i]; // 객체 배열을 하나씩 순회하며 _obs 클래스 변수에 대입
        		if(obs.getArriveTime()==real_time) { // _obs 객체의 도착 시간과 현재 시간이 동일하면
        			readyQueue0.add(obs);
        		}
        	}
        	
        	if(curPs != null && !curPs.checkReturn()) { // 현재 실행되고 있는 프로세스가 있고 그 프로세스가 종료되지 않았다면
        		curPs.execTime++;
        		curPs.leftServiceTime--;
        		if(q[j] == Math.pow(2,j)) {
        			readyQueue1 = readyQueue0;
        			readyQueue0.remove(curPs);
        			curPs=null;
        		}
        	}
        	if(curPs!=null&&!curPs.checkReturn()&&curPs.leftServiceTime==0)
        	
        	if(curPs != null && curPs.checkReturn()) {// 현재 실행되는 프로세스가 있고 그 프로세스가 종료가 되었다면
        		for(var process:readyQueue0)
        			process.calc_waitTime(real_time);
        		
        		System.out.printf("| The End(%d sec)\t\t", real_time);
        		curPs.setReturnTime(real_time); // 수행 완료한 현재 프로세스의 반환 시간을 Update 시킴
        		
        		System.out.printf("PID : %2d |\t\tArrive Time : %2d |\t\tService Time : %2d |\t\tReturn Time : %2d |\t\tNormalized Return Time : %.2f |",
                        curPs.getProcessId(), curPs.getArriveTime(), curPs.getServiceTime(), real_time, (double)curPs.getReturnTime() / curPs.getServiceTime());
                System.out.println();
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

                readyQueue0.remove(curPs); // 수행 완료된 현재 프로세는 readyQueue에서 삭제
                num_Process--; // 수행해야 하는 프로세스의 개수를 1 감소
                curPs = null;
        	}
        	nextPs = readyQueue0.peek(); // 다음 프로세스를 readyQueue에서 peek() 연산을 통해 하나 가져옴
        	
        }
	}
}
