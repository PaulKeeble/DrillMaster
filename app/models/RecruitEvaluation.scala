package models

import java.util.Date
import java.util.Calendar

case class RecruitEvaluation(player:Player,trainings:List[(Date,Training)]) {
  
  def hasTrainings(expected:List[Training]) : Boolean = {
    val ts = trainings.map(tup => tup._2)
    expected.forall(t => ts.contains(t))
  }
}

object RecruitEvaluation {
  def recruitsMissingTrainingOneMonth = {
     val oneMonthAgo = Dates.oneMonthAgo
     val recruits = Player.recruits.filter(_.joined.compareTo(oneMonthAgo)<0 )
     val trainings = PlayerTraining.allTrainingsByPlayer
     
     val re =for( recruit <- recruits) yield {
       val ts = trainings(recruit).map(t => (t.date,t.training))
       
       RecruitEvaluation(recruit,ts)
     }
     
     re.filterNot(_.hasTrainings(Training.requiredRecruitTraining))
  }
}