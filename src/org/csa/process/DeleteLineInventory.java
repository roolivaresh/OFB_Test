package org.csa.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.compiere.model.MInventory;
import org.compiere.model.MInventoryLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 *	@author ininoles
 *  @version $Id: DeleteLineInventory.java $
 *  this class was switch for Rodrigo Olivares Hurtado 2023-06-22
 */
public class DeleteLineInventory extends SvrProcess
{
	/**
	 *  Prepare - e.g., get  .
	 */

	private int p_Inventory_ID;
	private int cantLine=0;
	private java.sql.Timestamp p_GuaranteeDate;
	private String p_IsGuaranteeDate;
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();

			if(name.equals("QtyDelete"))
				cantLine = element.getParameterAsInt();
			else if (name.equals("GuaranteeDate"))
				p_GuaranteeDate = element.getParameterAsTimestamp();
			else if (name.equals("IsGuaranteeDate"))
				p_IsGuaranteeDate = element.getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Inventory_ID=getRecord_ID();

	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		MInventory inv = new MInventory(getCtx(), p_Inventory_ID, get_TrxName());
		MInventoryLine[] iLines = inv.getLines(false);

		//Rodrigo Olivares Hurtado 2023-06-23
		//Se agrega parametro para que funcionen dos procesos tanto el de eliminar linea por fecha de vencimineto del lote
		//y el de eliminar lineas de forma aleatoria

		if(p_IsGuaranteeDate.equals("Y")) {
			//Rodrigo OLivares Hurtado 2023-06-22
			//Se realiza ajuste en el proceso para que borre las lineas con fecha de vencimiento del lote
			//superior a la que se pasa por parametro.


			// Se valida de que ingrese una fecha
			if(p_GuaranteeDate != null) //if(cantLine > 0)
			{
				String InventoryLine = "0";
				for (MInventoryLine lines : iLines) {
					//La linea debe presentar un producto
					if (lines.getM_Product_ID() > 0 ) {
						//se valida que la linea tenga una instancia de conjunto de atributos y que la fecha sea superior
						//a la que se pasa por parametro
						int a = lines.getM_AttributeSetInstance_ID();
						//Rodrigo Olivares Hurtado 2023-07-12 se valida que la fecha de vencimiento no este vacia(null)
						//en el caso de que sea null se pone la fecha pasada por parametro para eliminar el producto
						java.sql.Timestamp LineGuaranteeDate = null;
						if (lines.getM_AttributeSetInstance().getGuaranteeDate() == null) {
							 Calendar date = Calendar.getInstance();
							 date.setTime(p_GuaranteeDate);
							 date.add(Calendar.YEAR, +1);

							 java.sql.Timestamp tm=new java.sql.Timestamp(date.getTimeInMillis());


							 LineGuaranteeDate=  tm;
						}else {
							LineGuaranteeDate =lines.getM_AttributeSetInstance().getGuaranteeDate();
						}

						if((lines.getM_AttributeSetInstance_ID() != 0 && p_GuaranteeDate.before(LineGuaranteeDate) )
								|| lines.getM_AttributeSetInstance_ID() == 0 ) {
							//Se registra los InventoryLineID que cumplan la condicion en la variable InventoryLine
							InventoryLine= InventoryLine+ ","+lines.get_ID();
						}

					}

				}
				log.info("InventoryLine_IDs "+ InventoryLine);
				//borrado de lineas
				//que cumplan las condiciones mecionadas anteriormente segun M_Inventory_ID y las lines
				//rescatadas en la variable InventoryLine
				DB.executeUpdate("DELETE FROM M_InventoryLine "
						+ " WHERE M_Inventory_ID = "+inv.get_ID()+" AND M_InventoryLine_ID  IN ("+InventoryLine+")", get_TrxName());
			}

		} else if(p_IsGuaranteeDate.equals("N")) {
			//Rodrigo Olivares Hurtado 2023-06-30
			// Se cambio de eliminar por lineas a eliminar por Productos.
			//se cambia validación de cantidad de lineas a cantidad de productos
			int cantproduct = DB.getSQLValue(get_TrxName(), "SELECT coalesce(count(DISTINCT(M_Product_ID)),0) as M_Product_ID "
					+ "						FROM M_InventoryLine "
					+ "					 WHERE M_Inventory_ID ="+ inv.get_ID());
			if(cantLine > cantproduct)
				throw new AdempiereException("ERROR: Parametro Incorrecto");

			if(cantLine > 0)
			{
				//se llena lista con numeros de id de productos
				String ID_NoDelete = "0";
				//llenado de id de productos que no se borraran
				String sql = "SELECT DISTINCT(M_Product_ID) as M_Product_ID " +
						" FROM M_InventoryLine " +
						" WHERE M_Inventory_ID ="+inv.get_ID();

				List<Integer> ID_productos = new ArrayList<>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(sql, get_TrxName());
					rs = pstmt.executeQuery();
					while (rs.next())
					{
						ID_productos.add(rs.getInt("M_Product_ID"));
					}
					rs.close();
					pstmt.close();
					pstmt = null;
				}
				catch (SQLException e)
				{
					throw new DBException(e, sql);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				//Rodrigo Olivares Hurtado 2023-06-30
				// se agrega nueva lista de productos que va registrando a medida que el metodo aleatorio lo seleciona
				//en el ciclo for
				List<Integer> ID_producto2 = new ArrayList<>();
				//el ciclo for se ejecutara hasta que la cantidad de productos sea igual a la solicitada
				for (int i = 0; ID_producto2.size()+1<= cantLine  ; i++)
				{
					int IDProd = ID_productos.get(numeroAleatorioEnRango(0, ID_productos.size() - 1));
					int Index = ID_NoDelete.indexOf(String.valueOf(IDProd));
					//Rodrigo Olivares Hurtado 2023-06-30
					//Se valida que el producto no este registrado ya en la lista
					if( Index ==  -1) {
						ID_NoDelete = ID_NoDelete+","+IDProd;
						ID_producto2.add(IDProd);
					}
				}
				//borrado de lineas
				DB.executeUpdate("DELETE FROM M_InventoryLine "
						+ " WHERE M_Inventory_ID = "+inv.get_ID()+" AND M_Product_ID NOT IN ("+ID_NoDelete+")", get_TrxName());
			}

		}


		return "Lineas Actualizadas";
	}

	public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con l�mite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
}
