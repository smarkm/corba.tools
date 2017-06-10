package org.smark.corba.tools.core;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
public class NamingSearch {
	public static List<String> names(String initialPort)  {
		String[] args = {"-ORBInitialPort",initialPort};
		ORB orb = ORB.init(args, null );
		
		List<String> names = new ArrayList<String>();
		String parent = "";
			Object object;
			try {
				object = orb.resolve_initial_references("NameService");
			NamingContextExt ctx = NamingContextExtHelper.narrow(object);
			 final int batchSize = 1000;
		      BindingListHolder bList = new BindingListHolder( );
		      BindingIteratorHolder bIterator = new BindingIteratorHolder( );

		      ctx.list( batchSize, bList, bIterator );

		      for ( int i=0; i < bList.value.length; i++ ) {
		        NameComponent[] name = { bList.value[i].binding_name[0] };
				if (bList.value[i].binding_type == BindingType.ncontext) {
		            NamingContext context = NamingContextHelper.narrow( 
		                ctx.resolve( name ) );
		            printContext( context, parent + name[0].id + "." );
		        } else {
		            //System.out.println( parent + name[0].id );
		            names.add(name[0].id);
		        }
		      }
			} catch (Exception e) {
				e.printStackTrace();
			}
		return names;
	}
	
	/**
	 * MySMServiceAppService_logger
		EMSServiceAppService
		SanityChecker
		EMSService_logger
		MySMServiceAppService_notifier
		ServiceLocator
		EMSServiceAppService_logger
		EMSService_Internal
		EMSOM_localhost
		MySMService
		EMSService
		MySMServiceAppService
		EMSServiceAppService_notifier

	 * @throws Exception
	 */
	public static List<RegistEntity> loadIORs(String serverHost,String initialPort)  {
		List<RegistEntity> reEntities = new ArrayList<RegistEntity>();
		String[] args = {"-ORBInitialPort",initialPort,"-ORBServerHost",serverHost};
		ORB orb = ORB.init(args, null );
		
		Object object;
		try {
			object = orb.resolve_initial_references("NameService");
			NamingContextExt ctx = NamingContextExtHelper.narrow(object);
			
			for (String name : names(initialPort)) {
				Object object2 = ctx.resolve_str(name);
				
				reEntities.add(new RegistEntity(name, object2.toString()));
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reEntities;
	}
	
	 public static void printContext( NamingContext nc, String parent )
	  { 
	    try {
	      final int batchSize = 1000;
	      BindingListHolder bList = new BindingListHolder( );
	      BindingIteratorHolder bIterator = new BindingIteratorHolder( );

	      nc.list( batchSize, bList, bIterator );

	      for ( int i=0; i < bList.value.length; i++ ) {
	        NameComponent[] name = { bList.value[i].binding_name[0] };
	        if (bList.value[i].binding_type == BindingType.ncontext) {
	            NamingContext context = NamingContextHelper.narrow( 
	                nc.resolve( name ) );
	            printContext( context, parent + name[0].id + "." );
	        } else {
	            System.out.println( parent + name[0].id );
	        }
	      }

	    } catch (Exception e) {
	        System.out.println("ERROR : " + e) ;
	    }
	  }

	public static void main(String[] args) {
		for (RegistEntity entity : loadIORs("127.0.0.1","2050")) {
			System.out.println(entity);
		}
	}
}
