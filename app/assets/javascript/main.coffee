successFn = (data) -> console.log(data)

errorFn = (error) -> console.log(error)

handlers = {
  success: successFn,
  error: errorFn
}

deletePlayerAjax = (userName) -> jsRoutes.controllers.Players.delete(userName).ajax(handlers)

getParentDiv = (btnObj) ->
    $(btnObj.target).parents("div.player")
   
getPlayerName = ($div) -> 
    $playerName = $div.find('input[id="name"]').val()

deletePlayer = (btnObj) ->
    $parentDiv = getParentDiv(btnObj)
    playerName = getPlayerName($parentDiv)
    $parentDiv.css('background-color','#FF5A55').fadeOut('slow',->$parentDiv.remove())
    deletePlayerAjax(playerName)


deleteTrainingAjax = (training) -> jsRoutes.controllers.Trainings.delete(training).ajax(handlers)

deleteTraining = (btnObj) ->
	$parentDiv =$(btnObj.target).parents("div.training")
	trainingName = btnObj.target.id
	$parentDiv.css('background-color','#FF5A55').fadeOut('slow',->$parentDiv.remove())
	deleteTrainingAjax(trainingName)

trainPlayerAjax = (player,training,date,trainer) ->
	d = { 'player': player, 'training': training, 'date': date, 'trainer': trainer }
	c = {
	    contentType: 'application/json',
	    data: JSON.stringify(d),
	    success : successFn,
	    error : errorFn
	}
	jsRoutes.controllers.PlayerTrainings.train().ajax(c)

trainPlayer = (btnObj) ->
	$parentDiv= $(btnObj.target).parents("div.simplePlayer")
	playerName = $parentDiv.find('p').text()
	trainingName = $('#training').val()
	date = $('#date').val()
	trainer = $('#trainer').val()
	trainPlayerAjax(playerName,trainingName,date,trainer)
	$parentDiv.css('background-color','#5AD055')


$(document).ready(()-> 
	$(".deletePlayerButton").on('click', (btnObj) -> deletePlayer(btnObj))
	$(".deleteTrainingButton").on('click', (btnObj) -> deleteTraining(btnObj))
	$(".trainButton").on('click', (btnObj) -> trainPlayer(btnObj))
)