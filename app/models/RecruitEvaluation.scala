package models

import java.util.Date
import java.util.Calendar
import dates.ComparableDate

case class RecruitEvaluation(player:Player,trainings:List[(Date,Training)]) {
  
  def hasTrainings(expected:List[Training]) : Boolean = {
    val ts = trainings.map(tup => tup._2)
    expected.forall(t => ts.contains(t))
  }
}

object RecruitEvaluation {
  private def evaluateRecruits(recruits:List[Player]) = {
    val trainings = PlayerTraining.allTrainingsByPlayer
     
    for( recruit <- recruits) yield {
      val ts = trainings.get(recruit).map{ _.map(t => (t.date,t.training)) }
       
      ts match {
        case Some(trainings) => RecruitEvaluation(recruit,trainings)
        case None => RecruitEvaluation(recruit,List())
      }
    } 
  }
  
  def recruitsMissingTrainingOneMonth = {
     val oneMonthAgo = Dates.oneMonthAgo
     val twoMonthsAgo = Dates.twoMonthsAgo
     val recruits = Player.recruits.filter(r => twoMonthsAgo <= r.joined && r.joined < oneMonthAgo)
     val evaluations = evaluateRecruits(recruits)
     evaluations.filterNot(_.hasTrainings(Training.requiredRecruitTraining))
  }
  
  def recruitsMissingTrainingTwoMonths = {
     val twoMonthsAgo = Dates.twoMonthsAgo
     val recruits = Player.recruits.filter(r => r.joined < twoMonthsAgo)
     val evaluations = evaluateRecruits(recruits)
     evaluations.filterNot(_.hasTrainings(Training.requiredRecruitTraining))
  }
  
  def recruitsMeetingPromotion = {
    val oneMonthAgo = Dates.oneMonthAgo
    val recruits = Player.recruits.filter(r => r.joined < oneMonthAgo)
    val evaluations = evaluateRecruits(recruits)
    evaluations.filter(_.hasTrainings(Training.requiredRecruitTraining))
  }
}