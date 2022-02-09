import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainClass {

	public static void main(String[] args) throws IOException {
		
		String input = "[[0,3],[15,18],[17,20],[2,10]]";
		
		System.out.println("Please enter set of processes in below format: ");
		System.out.println(input + "\nInput:");
		
		// Enter data using BufferReader
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
 
        // Reading data using readLine
        input = reader.readLine();
		
		if(input.startsWith("[[") && input.endsWith("]]") && input.length() > 4) {
		
		String truncatedInp = input.substring(2, input.length()-2);
		
		List<String> processStrList = Arrays.asList(truncatedInp.split("],\\["));
		
		List<Process> processList = createProcessList(processStrList);
		
		
		List<Machine> machinesAndProcesses = new ArrayList<Machine>();
		machinesAndProcesses.add(new Machine());
		
		Collections.sort(processList);
		
		assignProcessToMachine(machinesAndProcesses, processList);
		
		// System.out.println("Final machinesAndProcessed: "+ machinesAndProcesses);
		
		System.out.println("Output:\nTotal Machines Required: "+machinesAndProcesses.size());
		} else {
			System.out.println("Invalid input.");
		}

	}
	
	static List<Process> createProcessList (List<String> processStrList){
		List<Process> createdProcessList = new ArrayList<>();
		processStrList.forEach(eachStr -> {
			Process prc = new Process();
			String[] timesPerProcess = eachStr.split(",");
			if(timesPerProcess.length == 2) {
				prc.startTime = Integer.parseInt(timesPerProcess[0].trim());
				prc.endTime = Integer.parseInt(timesPerProcess[1].trim());
			}
			createdProcessList.add(prc);
		});
		return createdProcessList;
	}
	
	static void assignProcessToMachine (List<Machine> machinesAndProcesses, List<Process> processList) {
		processList.forEach(prc -> {
			boolean isMachinesAccommodable = false;
			for(Machine machine : machinesAndProcesses) {
				isMachinesAccommodable = canMachineAccomodateCurrentPrc(machine.processListofMachine, prc);
				if(isMachinesAccommodable) {
					machine.processListofMachine.add(prc);
					Collections.sort(machine.processListofMachine);
					machine.lastEndTime = machine.processListofMachine.get(machine.processListofMachine.size()-1).endTime;
					break;
				}
			}
			if(!isMachinesAccommodable) {
				Machine newMachine = new Machine();
				newMachine.processListofMachine.add(prc);
				machinesAndProcesses.add(newMachine);
				Collections.sort(machinesAndProcesses);
			}
			Collections.sort(machinesAndProcesses);
		});
	}
	
	static boolean canMachineAccomodateCurrentPrc (List<Process> machine, Process prc) {
		int previousEndTime = -1;
		if(machine.size() == 0 || (machine.size() > 0 && (prc.endTime < machine.get(0).startTime || machine.get(machine.size()-1).endTime < prc.startTime))) {
			return true;
		}
		for(Process machinePrc : machine) {
			if(previousEndTime < prc.startTime && prc.endTime < machinePrc.startTime) {
				return true;
			}
			previousEndTime = machinePrc.endTime;
		}
		return false;
	}
	
}

class Process implements Comparable<Process>{
	
	int startTime;
	
	int endTime;
	
	@Override
    public String toString() {
        return "startTime: "+this.startTime + " endTime: " + this.endTime;
    }

	@Override
	public int compareTo(Process arg) {
		return Integer.compare(this.startTime, arg.startTime);
	}
	
}

class Machine implements Comparable<Machine>{
	
	List<Process> processListofMachine = new ArrayList<Process>();
	
	int lastEndTime;
	
	@Override
    public String toString() {
        return "processListofMachine: "+this.processListofMachine + " lastEndTime: " + this.lastEndTime;
    }

	@Override
	public int compareTo(Machine arg) {
		return Integer.compare(this.lastEndTime, arg.lastEndTime);
	}
	
}