successFn = (data) -> 

errorFn = (error) -> 

handlers = {
  success: successFn,
  error: errorFn
}

deletePlayerAjax = (userName) -> jsRoutes.controllers.Players.delete(userName).ajax(handlers)

deleteTrainingAjax = (training) -> jsRoutes.controllers.Trainings.delete(training).ajax(handlers)

getParentDiv = (btnObj) ->
    $(btnObj.target).parents("div.player")
   
getPlayerName = ($div) -> 
    $playerName = $div.find('input[id="name"]').val()

deletePlayer = (btnObj) ->
    $parentDiv = getParentDiv(btnObj)
    playerName = getPlayerName($parentDiv)
    $parentDiv.css('background-color','#FF5A55').fadeOut('slow',->$parentDiv.remove())
    deletePlayerAjax(playerName)
    
deleteTraining = (btnObj) ->
	$parentDiv =$(btnObj.target).parents("div.training")
	trainingName = btnObj.target.id
	$parentDiv.css('background-color','#FF5A55').fadeOut('slow',->$parentDiv.remove())
	deleteTrainingAjax(trainingName)

$(document).ready(()-> 
	$(".deletePlayerButton").on('click', (btnObj) -> deletePlayer(btnObj))
	$(".deleteTrainingButton").on('click', (btnObj) -> deleteTraining(btnObj))
)