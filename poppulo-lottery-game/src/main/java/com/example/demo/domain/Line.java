package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Arrays;

@Entity
public class Line implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	private int[] numbers = new int[3];

	public Line() {
	}

	public Line(int[] numbers) {
		this.numbers = numbers;
	}

	public Long getId() {
		return id;
	}

	public int[] getNumbers() {
		return numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	public int calculateResult(){
		if(numbers[0]+numbers[1]+numbers[2]==2){
			return 10;
		}else if(numbers[0]==numbers[1] && numbers[1]==numbers[2]){
			return 5;
		}else if(numbers[0]!=numbers[1] && numbers[0] != numbers[2]){
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Line{" +
				"lineId=" + id +
				", numbers=" + Arrays.toString(numbers) +
				'}';
	}
}
