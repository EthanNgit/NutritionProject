# NutritionProject
Full Stack Android Mobile Application.

Current Version:
* V: 0.3.1.1 Switched completely to scalable dp units (I found out the hard way), also started hosting db on droplet.

## Past versions
> Documentation of each git release is contained and stored.

<details><summary>History</summary>
 <details><summary>V3</summary>
  <ul>
   <li> V: 0.3 Heavy php update, added macro counting and user history functionality</li>
  </ul>
 </details>
 <details><summary>V2</summary>
  <ul>
   <li> 0.2.3.1 Added add and search functionality (still need ui), took years to debug gson and php. Added more event functionality, and more dev qol (had calc test, will get back soon) </li>
   <li> 0.2.2.1 Added Barcode scanner after 6 tries (Had to use Kotlin, could not get it right with java), still needs more QOL, for better scanning UX </li>
   <li> 0.2.1.1 Fixed some last second food object problems I figured out (serving size) </li>
   <li> V: 0.2 Food and nutrient Objects, along with there serialization adapters, started the barcode scanner. </li>
  </ul>
 </details>
 <details><summary>V1</summary>
  <ul>
   <li> 0.1.4.2 / 0.1.4.3 Fixing a git error i made and finishing up </li>
   <li> 0.1.4.1 Added reset password and otp generation (for security reasons probly shift it over to php hosting otp instead, but ill leave that out as of now) </li>
   <li> 0.1.3.1 Added most menus, added auto generated profile picture </li>
   <li> 0.1.2.1 Added settings, logout, and more ui. </li>
   <li> 0.1.1.1 Added manual TDEE option, finished setting base dashboard up</li>
   <li> V: 0.1 Login / Register System with mySQL database Android preferences for Remember me TDEE Calculator</li>
  </ul>
 </details>
</details>

## Future plans
> Plans and structure for each version planned along with the already completed versions.

<details><summary>Roadmap</summary>
 <details><summary>V1</summary>
  <ul>
   <li> - [] V: 0.3 Heavy php update, added macro counting and user history functionality</li>
  </ul>
 </details>
 <details><summary>V2</summary>
  <ul>
   <li> Base Login system (Register / Login / Remember me / Forgot Password) </li>
   <li> Tdee calculator </li>
   <li> Dashboard ui  </li>
   <li> Other base uis (profile, settings, other smaller menus...) </li>
  </ul>
 </details>
 <details><summary>V3</summary>
  <ul>
   <li> Food item database system </li>
   <li> Adding functionality </li>
   <li> Searching functionality </li>
   <li> Barcode functionality </li>
   <li> Adding / Searching ui (search lists and add forms) </li>
  </ul>
 </details>
 <details><summary>V4</summary>
  <ul>
   <li> Macro counting </li>
   <li> Meal Planner </li>
   <li> User history </li>
   <li> Object based personal recipe items </li>
   <li> Fix mobile port ui </li>
   <li> Add alcohol macro </li>
   <li> Nutrition label scanner </li>
  </ul>
 </details>
 <details><summary>V5</summary>
  <ul>
   <li> Data analysis </li>
   <li> Meal History </li>
   <li> Day review </li>
  </ul>
 </details>
 <details><summary>V6</summary>
  <ul>
   <li> Undecided (Most likely polish and implementing some potential features if I feel the need to) </li>
  </ul>
 </details>
</details>



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
   




