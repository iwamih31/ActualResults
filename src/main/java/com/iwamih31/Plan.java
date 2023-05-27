package com.iwamih31;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {

  // ID
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer id;

  // 利用者ID
  @Column(name = "user_id", nullable = false)
  private Integer user_id;

  // プラン適用開始日
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM")   // 出力時の期待フォーマット
  @Column(name = "start_date", nullable = false)
  private LocalDate start_date;

  // プラン適用終了日
  @DateTimeFormat(pattern = "yyyy-MM-dd")   // 入力時の期待フォーマット
  @JsonFormat(pattern = "yyyy/MM")   // 出力時の期待フォーマット
  @Column(name = "last_date", nullable = false)
  private LocalDate last_date;

  // 曜日
  @Column(name = "day_of_week", nullable = false)
  private String day_of_week;

  // サービス内容
  @Column(name = "subject", nullable = false)
  private String subject;

  // サービス開始時刻
  @Column(name = "start_time", nullable = false)
  private String start_time;

  // サービス提供時間（分）
  @Column(name = "minutes", nullable = false)
  private Integer minutes;

  // サービス内訳
  @Column(name = "items", nullable = false)
  private String items;

  // 備考
  @Column(name = "note", nullable = false)
  private String note;

  // コピーコンストラクタ
  public Plan(Plan plan) {
    this.id = plan.id;
    this.user_id = plan.user_id;
    this.start_date = plan.start_date;
    this.last_date = plan.last_date;
    this.day_of_week = plan.day_of_week;
    this.subject = plan.subject;
    this.start_time = plan.start_time;
    this.minutes = plan.minutes;
    this.items = plan.items;
    this.note = plan.note;
  }

}
