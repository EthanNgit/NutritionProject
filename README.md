# NutritionProject
Full Stack Android Mobile Application.
> Current version: V: 0.5 bug fixes, polish, icon, app name...

## About
This application is built similarly to other fitness apps, with some improvments I personally would like in a fitness app. It contains more ease of use when it comes to adding items to your daily meals compared to other apps. The app contains a custom database that will be able to house thousands of items for users to search for and add into their macros. The app skips over the useless mumbo jumbo that make other apps clunky like recipes and workouts. Realistically workouts have nothing to do counting macros directly (not accounting for the extra tdee), as in they do not need to suggest me to do 15 sets of crunches 3 times a day, it is outside the scope of a nutritional app. The app takes in the least amount of personal data possible, just an email and a password. This is for the users privacy, as why do I need a name, I only need to be able to associate your account, which can be done with those only. There is a built in tdee calculator that compares in accuracy to other online ones. There is a lot of details skimmed over in this section, but I beleive it to be enough to breifly introduce you to my application 
### Tools used
  <p float="left">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/android/android-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/androidstudio/androidstudio-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/kotlin/kotlin-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/xml/xml-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mariadb/mariadb-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/php/php-original.svg" width="25">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/digitalocean/digitalocean-original.svg" width="25"/>
</p>

## Walkthrough
<img src="https://github.com/EthanNgit/NutritionProject/assets/105979510/e2b45f4f-51b6-4afd-9137-951c75d87230" width="1000">


## Past versions
> Documentation of each git release is contained and stored.

<details><summary>History</summary>
   <details><summary>V4</summary>
  <ul>
    <li> V: 0.4.1.1 Finished all basic statistical analysis and ui funcitonality</li>
    <li> V: 0.4 Base statistical ui</li>
  </ul>
 </details>
 <details><summary>V3</summary>
  <ul>
    <li> V: 0.3.4.1 Meal system, polish, finished up.</li>
    <li> V: 0.3.3.1 Code cleanup, changed styles to something more managable, and few bug fixes with the previous update.</li>
    <li> V: 0.3.2.1 Massive. Theme update. Nutrition scanner integration. Basic macro statistics. Meal objects / Recipe objects. Php update. etc.</li>
    <li> V: 0.3.1.1 Switched completely to scalable dp units (I found out the hard way), also started hosting db on droplet.</li>
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
   <li> :heavy_check_mark: Base Login system (Register / Login / Remember me / Forgot Password) </li>
   <li> :heavy_check_mark: Tdee calculator </li>
   <li> :heavy_check_mark: Dashboard ui  </li>
   <li> :heavy_check_mark: Other base uis (profile, settings, other smaller menus...) </li>
  </ul>
 </details>
 <details><summary>V2</summary>
  <ul>
   <li> :heavy_check_mark: Food item database system </li>
   <li> :heavy_check_mark: Adding functionality </li>
   <li> :heavy_check_mark: Searching functionality </li>
   <li> :heavy_check_mark: Barcode functionality </li>
   <li> :heavy_check_mark: Adding / Searching ui (search lists and add forms) </li>
  </ul>
 </details>
 <details><summary>V3</summary>
  <ul>
   <li> :heavy_check_mark: Macro counting </li>
   <li> :heavy_check_mark: Meal Planner </li>
   <li> :heavy_check_mark: Add meal layout </li>
   <li> :heavy_check_mark: Meal view layout </li>
   <li> :heavy_check_mark: User history </li>
   <li> :heavy_check_mark: Object based personal recipe items </li>
   <li> :heavy_check_mark: Fix mobile port ui </li>
   <li> :heavy_check_mark: Add alcohol macro </li>
   <li> :heavy_check_mark: Nutrition label scanner </li>
  </ul>
 </details>
 <details><summary>V4</summary>
  <ul>
   <li> :heavy_check_mark: Data analysis </li>
   <li> :heavy_check_mark: Meal History </li>
   <li> :heavy_check_mark: Day review </li>
  </ul>
 </details>
 <details><summary>V5</summary>
  <ul>
   <li> :x: Search page recent meals</li>
   <li> :x: Profile details page</li>
   <li> :x: Profile tos page</li>
   <li> :x: Profile cerdits page</li>
   <li> :x: code cleaning</li>
   <li> :x: last minute bug catches</li>
  </ul>
 </details>
 <details><summary>Potential plans</summary>
  <ul>
   <li> :x: Search settings (would have to create a new relationship table in db to then be able to sort by those instead of the json method in use currently) </li>
   <li> :x: Database based reset password </li>
   <li> :x: Resarch into more security for the database </li>
   <li> :x: Add a barcode api to show what the item would be if it isnt added </li>
   <li> :x: Stronger / More user friendly search algorithm </li>
   <li> :x: Security update (given that i were inclined to publicly release this, there is some must-do changes) </li>
   <li> :x: Api for the food database (most likely to happen, with python) </li>
   <li> :x: Ordering options for widgets </li>
   <li> :x: Expanded details for widgets </li>
   <li> :x: Weight loss widgets / more widgets </li>
   <li> :x: Admin console for regulating db (most likely to happen, with wpf c#) </li>
  </ul>
 </details>
  <details><summary>Passive plans</summary>
  <ul>
   <li> :x: Switch to a strings xml layout (also dimesions) </li>
   <li> :x: Switch "Custom...Method" classes to static (so events can be called on multiple layers at once) </li>
   <li> :x: Create more event callbacks (and be more descriptive) </li>
   <li> :x: Switch to php (server side) local time </li>
   <li> :heavy_check_mark: Switching to sdp and ssp, just found out, this is a large chunk of time i need to reinvest... </li>
 </details>
</details>

## Resources
> I would not of been able to get so far without the thanks of these.
*  [Google ML kit (Nutrition scanner & Barcode scanner)](https://developers.google.com/ml-kit)
*  [Retrofit 2](https://square.github.io/retrofit/)
*  [Scalable dp](https://github.com/intuit/sdp)
*  [Scalable sp](https://github.com/intuit/ssp)
*  [Semi-circle progress bar](https://github.com/hadibtf/SemiCircleArcProgressBar)
*  [Single row calender](https://github.com/miso01/SingleRowCalendar)
  


