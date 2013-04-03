successFn = (data) -> 

errorFn = (error) -> 

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

$(document).ready(()-> 
	$(".deleteButton").on('click', (btnObj) -> deletePlayer(btnObj))
)