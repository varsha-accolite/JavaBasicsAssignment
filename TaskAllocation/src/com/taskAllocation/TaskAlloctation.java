package com.taskAllocation;

import java.util.*;

import java.io.*;
import java.time.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskAlloctation {
	static List<Plan> getCancelledPeriodsForTask(List<Plan> oldPlanList, List<Plan> newPlanList){
		List<Plan> res = new ArrayList<>();
		//DateTime dtOrg = new DateTime(dt);
		//DateTime dtPlusOne = dtOrg.plusDays(1);
		Collections.sort(oldPlanList, new Comparator<Plan>() {
	        @Override
	        public int compare(Plan task1, Plan task2) {
	        	if(task1.getTaskId() == task2.getTaskId()) {
		            if (task1.getStartDate().equals(task2.getStartDate())) {
		                return task1.getEndDate().compareTo(task2.getEndDate());
		            }
		            return task1.getStartDate().compareTo(task2.getStartDate());
	        	}
	        	return task1.getTaskId()-task2.getTaskId();
	        }
	    });
		Collections.sort(newPlanList, new Comparator<Plan>() {
	        @Override
	        public int compare(Plan task1, Plan task2) {
	        	if(task1.getTaskId() == task2.getTaskId()) {
		            if (task1.getStartDate().equals(task2.getStartDate())) {
		                return task1.getEndDate().compareTo(task2.getEndDate());
		            }
		            return task1.getStartDate().compareTo(task2.getStartDate());
	        	}
	        	return task1.getTaskId()-task2.getTaskId();
	        }
	    });
		HashMap<Integer, ArrayList<Plan>> hOld = new HashMap<>();
		for(Plan p : oldPlanList) {
			if(hOld.containsKey(p.getTaskId())) {
				ArrayList<Plan> x = hOld.get(p.getTaskId());
				x.add(p);
				hOld.put(p.getTaskId(), x);
			}
			else {
				ArrayList<Plan> x = new ArrayList<>();
				x.add(p);
				hOld.put(p.getTaskId(), x);
			}
		}
		HashMap<Integer, ArrayList<Plan>> hNew = new HashMap<>();
		for(Plan p : newPlanList) {
			if(hNew.containsKey(p.getTaskId())) {
				ArrayList<Plan> x = hNew.get(p.getTaskId());
				x.add(p);
				hNew.put(p.getTaskId(), x);
			}
			else {
				ArrayList<Plan> x = new ArrayList<>();
				x.add(p);
				hNew.put(p.getTaskId(), x);
			}
		}
		for(Map.Entry<Integer, ArrayList<Plan>> h : hOld.entrySet()) {
			int taskId = h.getKey();
			ArrayList<Plan> pOld = h.getValue();
			Collections.sort(pOld, new Comparator<Plan>() {
			    @Override
			    public int compare(Plan task1, Plan task2) {
				    if (task1.getStartDate().equals(task2.getStartDate())) {
				       return task1.getEndDate().compareTo(task2.getEndDate());
				    }
				    return task1.getStartDate().compareTo(task2.getStartDate());
			    } 	
		    });
			ArrayList<Plan> pNew = hNew.get(taskId);
			for(int j = 0; j < pNew.size(); j++) {
				int i = 0;
		        if(pNew.get(j).getEndDate().compareTo(pOld.get(0).getStartDate()) < 0)
		            break;
		        while(i < pOld.size() && pOld.get(i).getEndDate().compareTo(pNew.get(j).getStartDate())<0)
		            i++;
		        if(i == pOld.size())
		            break;
		        if(pNew.get(j).getStartDate().compareTo(pOld.get(i).getStartDate()) <= 0 ){
		            if(pNew.get(j).getEndDate().compareTo(pOld.get(i).getEndDate()) >= 0 )
		                pOld.remove(i);
		            else {
		            	Plan x = pOld.get(i);
		            	Date dt = pNew.get(j).getEndDate();
		            	LocalDateTime.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusDays(1);
		                x.setStartDate(dt);
		            }
		            break;
		        }
		        else if(pNew.get(j).getStartDate().compareTo(pOld.get(i).getStartDate()) > 0 &&  pNew.get(j).getEndDate().compareTo(pOld.get(i).getEndDate()) < 0) {
		            Plan x = new Plan();
	            	Date dt = pNew.get(j).getEndDate();
	            	LocalDateTime.from(dt.toInstant().atZone(ZoneId.of("UTC"))).plusDays(1);
	                x.setStartDate(dt);
	                x.setEndDate(pOld.get(i).getEndDate());
		            x.setTaskId(pOld.get(i).getTaskId());
		            pOld.add(x);
		            Plan y = pOld.get(i);
	            	Date dt2 = pNew.get(j).getStartDate();
	            	LocalDateTime.from(dt.toInstant().atZone(ZoneId.of("UTC"))).minusDays(1);
	                y.setEndDate(dt2);
		            break;
		        }
		        else {
		        	Plan x = pOld.get(i);
	            	Date dt = pNew.get(j).getStartDate();
	            	LocalDateTime.from(dt.toInstant().atZone(ZoneId.of("UTC"))).minusDays(1);
	                x.setEndDate(dt);
		            break;
		        }

			}
			res.addAll(pOld);
		}
		Collections.sort(res, new Comparator<Plan>() {
	        @Override
	        public int compare(Plan task1, Plan task2) {
	        	if(task1.getTaskId() == task2.getTaskId()) {
		            if (task1.getStartDate().equals(task2.getStartDate())) {
		                return task1.getEndDate().compareTo(task2.getEndDate());
		            }
		            return task1.getStartDate().compareTo(task2.getStartDate());
	        	}
	        	return task1.getTaskId()-task2.getTaskId();
	        }
	    });
		return res;
	}
	public static void main(String args[]) throws IOException, ParseException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		System.out.println(" ");
		System.out.println("Enter no.of tasks in Old Plan");
		int n = Integer.parseInt(reader.readLine());
		List<Plan> oldPlan = new ArrayList<>();
		while(n-- > 0) {
			String s = reader.readLine();
			String[] str = s.trim().split(" ");
			int id = Integer.parseInt(str[0]);
			Date startDate = sdf.parse(str[1]);
		    Date endDate = sdf.parse(str[2]);
			Plan p = new Plan();
			p.setTaskId(id);
			p.setStartDate(startDate);
			p.setEndDate(endDate);
			oldPlan.add(p);
		}
		System.out.println("Enter no.of tasks in New Plan");
		int m = Integer.parseInt(reader.readLine());
		List<Plan> newPlan = new ArrayList<>();
		System.out.print("  ");
		while(m-- > 0) {
			String s = reader.readLine();
			String[] str = s.trim().split(" ");
			int id = Integer.parseInt(str[0]);
			Date startDate = sdf.parse(str[1]);
		    Date endDate = sdf.parse(str[2]);
			Plan p = new Plan();
			p.setTaskId(id);
			p.setStartDate(startDate);
			p.setEndDate(endDate);
			newPlan.add(p);
		}
		List<Plan> res = getCancelledPeriodsForTask(oldPlan, newPlan);
		for(int i = 0; i < res.size(); ++i) {
			Plan p = res.get(i);
			String startDate = sdf.format(p.getStartDate());
			String endDate = sdf.format(p.getEndDate());
			System.out.println(p.getTaskId()+" "+startDate+" "+endDate);
		}
	}
}
