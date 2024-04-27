package com.hibernate.demo;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class App {
	
    public static void main( String[] args )  {
    	
        SessionFactory sf = null;
        Session s = null;
        Transaction tx = null;
        CriteriaBuilder cb = null;
        
        try {
        	sf = new Configuration().configure().buildSessionFactory();
        	s  = sf.openSession();
        	tx = s.beginTransaction();
        	cb = s.getCriteriaBuilder();
        	
        /*	for(int i= 1; i<26;i++) {
        		Employee employee = createEmployee("Employee"+i,"Department"+(i%5),15000+(i*2000));
        		s.save(employee);
        	}  */
        	
        	CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        	
        	Root<Employee> root = query.from(Employee.class);
        	
        	query.select(root).where(cb.equal(root.get("department"), "Department1")).orderBy(cb.asc(root.get("salary")));
        	
        	TypedQuery<Employee> criteriaQuery = s.createQuery(query);
        	List<Employee> employees = criteriaQuery.getResultList();
        	
        	System.out.println("**************************");
        	
        	System.out.println(employees.size());
        	
        	for(Employee employee : employees) {
        		System.out.println(employee);
        	}
        	
        }
        catch(HibernateException e) {
        	if(tx != null)
        		tx.rollback();
        	e.printStackTrace();
        }catch (Exception e) {
        	if(tx != null)
        		tx.rollback();
        	e.printStackTrace();
		}
        finally {
        	if(tx != null)
        		tx.commit();
        	if(s != null)
        		s.close();
        	if(sf != null)
        		sf.close();
		}
    }
    private static Employee createEmployee(String firstName,String department,double salary) {
    	Employee employee = new Employee();
    	employee.setFirstName(firstName);
    	employee.setDepartment(department);
    	employee.setSalary(salary);
    	return employee;
    }
}
