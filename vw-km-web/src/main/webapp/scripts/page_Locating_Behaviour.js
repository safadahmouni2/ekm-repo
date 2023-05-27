
function PageLocating_Behaviour_Init() 
{
  if ($("controls_VehicleBrand"))
  {

		$("controls_VehicleBrand").onchange = function()
    {
      alert (this.value);
		}
  }
}
