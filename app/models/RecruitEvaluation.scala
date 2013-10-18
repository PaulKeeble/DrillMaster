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
     
     val re =for( recruit <- recruits) yield {
       val ts = trainings.get(recruit).map{ _.map(t => (t.date,t.training)) }
       
       ts match {
         case Some(trainings) => RecruitEvaluation(recruit,trainings)
         case None => RecruitEvaluation(recruit,List())
       }
     }
     
     re.filterNot(_.hasTrainings(Training.requiredRecruitTraining))
  }
  
  def recruitsMissingTrainingOneMonth = {
     val oneMonthAgo = Dates.oneMonthAgo
     val twoMonthsAgo = Dates.twoMonthsAgo
     val recruits = Player.recruits.filter(r => twoMonthsAgo <= r.joined && r.joined < oneMonthAgo)
     evaluateRecruits(recruits)
  }
  
  def recruitsMissingTrainingTwoMonths = {
     val twoMonthsAgo = Dates.twoMonthsAgo
     val recruits = Player.recruits.filter(r => r.joined < twoMonthsAgo)
     evaluateRecruits(recruits)
  }
}