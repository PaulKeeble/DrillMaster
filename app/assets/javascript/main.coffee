successFn = (data) -> 

errorFn = (error) -> 

handlers = {
  success: successFn,
  error: errorFn
}

deletePlayerAjax = (userName) -> jsRoutes.controllers.Players.delete(userName).ajax(handlers)

getParentDiv = (btnObj) ->
   $(btnObj.target).parents("div.player")
   
getPlayerName = (btnObj) ->
   $playerName = getParentDiv(btnObj).find('input[id="name"]')
   $playerName.val()
   

deletePlayer = (btnObj) ->
    $parentDiv = getParentDiv(btnObj)
    playerName = getPlayerName(btnObj)
    $parentDiv.css('background-color','#FF5A55').fadeOut('slow',->$parentDiv.remove())
    deletePlayerAjax(playerName)

$(document).ready(()-> 
	$(".deleteButton").on('click', (obj) -> deletePlayer(obj))
)