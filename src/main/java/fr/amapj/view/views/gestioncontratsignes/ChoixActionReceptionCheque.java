/*
 *  Copyright 2013-2014 AmapJ Team
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.view.views.gestioncontratsignes;

import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * 
 */
@SuppressWarnings("serial")
public class ChoixActionReceptionCheque extends SwitchPopup
{
	
	private Long mcId;

	/**
	 * 
	 */
	public ChoixActionReceptionCheque(Long mcId)
	{
		popupTitle = "Autres actions sur les paiements";
		popupWidth = "50%";
		
		this.mcId = mcId;

	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez faire :";

		addLine("Chercher les chèques à rendre aux amapiens", new PopupChercherChequeARendre(mcId));

	}

}
