package models

import play.api.test._
import play.api.test.Helpers._
import org.specs2.mutable.Specification
import org.junit.runner.Computer
import models._
import java.util.Date

class RecruitEvaluationsSpec extends Specification {
  def appWithMemoryDatabase() = FakeApplication(additionalConfiguration = inMemoryDatabase("test"))

  "a recruit is missing training" in new WithApplication(appWithMemoryDatabase()) {

    Player.create(new Player("name", "1234", "remark", Recruit, Dates.oneMonthAgo))
    val Some(player) = Player.find("name")
    Training.create("Basic Infantry")
    val Some(training) = Training.find("Basic Infantry")

    PlayerTraining.add(new PlayerTraining(player, training,new Date, "Trainer"))

    val recruits = RecruitEvaluation.recruitsMissingTrainingOneMonth

    recruits.map(_.player) must contain(player)
  }

  "a recruit has all training" in new WithApplication(appWithMemoryDatabase()) {
    Player.create(new Player("name", "1234", "remark", Recruit, Dates.oneMonthAgo))
    val Some(player) = Player.find("name")
    Training.create("Basic Infantry")
    val Some(basicTraining) = Training.find("Basic Infantry")
    Training.create("Advanced Infantry")
    val Some(advancedTraining) = Training.find("Advanced Infantry")
    
    PlayerTraining.add(new PlayerTraining(player, basicTraining, new Date, "Trainer"))
    PlayerTraining.add(new PlayerTraining(player, advancedTraining, new Date, "Trainer"))
    
    val recruits = RecruitEvaluation.recruitsMissingTrainingOneMonth
    
    recruits.map(_.player) must be(List())
  }
  
  //recruit hasn't reached a month yet
  "a new recruit is not listed" in new WithApplication(appWithMemoryDatabase()) {
    Player.create(new Player("name", "1234", "remark", Recruit, new Date))
    val Some(player) = Player.find("name")
    
    val recruits = RecruitEvaluation.recruitsMissingTrainingOneMonth
    
    recruits.map(_.player) must be(List())
  }
}