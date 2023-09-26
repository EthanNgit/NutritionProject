# NutritionProject
Full Stack Android Mobile Application.

Current Version:
* V: 0.3.1.1 Switched completely to scalable dp units (I found out the hard way), also started hosting db on droplet.

Past Versions:
* V: 0.3 Heavy php update, added macro counting and user history functionality
* 0.2.4.1 Added the base ui for search and add function base app is about finished, working on recipe next
* 0.2.3.1 Added add and search functionality (still need ui), took years to debug gson and php. Added more event functionality, and more dev qol (had calc test, will get back soon)
* 0.2.2.1 Added Barcode scanner after 6 tries (Had to use Kotlin, could not get it right with java), still needs more QOL, for better scanning UX
* 0.2.1.1 Fixed some last second food object problems I figured out (serving size)
* V: 0.2 Food and nutrient Objects, along with there serialization adapters, started the barcode scanner.
* 0.1.4.2 / 0.1.4.3 Fixing a git error i made and finishing up
* 0.1.4.1 Added reset password and otp generation (for security reasons probly shift it over to php hosting otp instead, but ill leave that out as of now)
* 0.1.3.1 Added most menus, added auto generated profile picture
* 0.1.2.1 Added settings, logout, and more ui.
* 0.1.1.1 Added manual TDEE option, finished setting base dashboard up
* V: 0.1 Login / Register System with mySQL database Android preferences for Remember me TDEE Calculator

Plans: 
 * V. 0.1 ✅
  * Base Login system (Register / Login / Remember me / Forgot Password) ✅
  * Tdee ✅
  * Dashboard ui ✅
  * Other base uis (profile, settings, other smaller menus...) ✅
 * V. 0.2 ✅
   * Food item database system ✅
   * Adding functionality ✅
   * Searching functionality✅
   * Barcode functionality ✅
   * Adding / Searching ui (search lists and add forms) ✅
 * V. 0.3
   * Macro counting ✅
   * Meal Planner
   * User history ✅
   * Object based personal recipe items
   * fix mobile port ui
   * add alcohol macro
 * V. 0.4
   * Data analysis
   * Meal History
   * Day review
 * V. 0.5
   * Undecided (Most likely polish and implementing some potential features if I feel the need to)

Potential Plans:
* Search settings (would have to create a new relationship table in db to then be able to sort by those instead of the json method in use currently)
* Database based reset password
* Resarch into more security for the database
* Add a barcode api to show what the item would be if it isnt added
* Stronger / More user friendly search algorithm
* Security update (given that i were inclined to publicly release this, there is some must-do changes)
* Api for the food database (most likely to happen, with python)
* Admin console for regulating db (most likely to happen, with wpf c#)
  * Ability to verify products easily
  * Ability to implement a check feature (someone adds item -> they use temperary version of it -> admin checks it -> admin can add it to db)

Passive Plans (Will do slowly / is on back burner):
* Switch to a strings xml layout (also dimesions)
* Switch "Custom...Method" classes to static (so events can be called on multiple layers at once)
* Create more event callbacks (and be more descriptive)
* Switch to php (server side) local time
* Switching to sdp and ssp, just found out, this is a large chunk of time i need to reinvest...
   




