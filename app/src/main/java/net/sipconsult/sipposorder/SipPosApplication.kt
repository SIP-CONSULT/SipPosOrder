package net.sipconsult.sipposorder

import android.app.Application
import net.sipconsult.sipposorder.data.datasource.location.local.*
import net.sipconsult.sipposorder.data.datasource.location.network.*
import net.sipconsult.sipposorder.data.datasource.order.local.*
import net.sipconsult.sipposorder.data.datasource.order.network.*
import net.sipconsult.sipposorder.data.datasource.product.local.*
import net.sipconsult.sipposorder.data.datasource.product.network.*
import net.sipconsult.sipposorder.data.datasource.salesAgent.local.*
import net.sipconsult.sipposorder.data.datasource.salesAgent.network.*
import net.sipconsult.sipposorder.data.datasource.user.*
import net.sipconsult.sipposorder.data.db.ApplicationDatabase
import net.sipconsult.sipposorder.data.network.*
import net.sipconsult.sipposorder.data.provider.*
import net.sipconsult.sipposorder.data.repository.location.*
import net.sipconsult.sipposorder.data.repository.order.*
import net.sipconsult.sipposorder.data.repository.product.*
import net.sipconsult.sipposorder.data.repository.salesAgent.*
import net.sipconsult.sipposorder.data.repository.user.*
import net.sipconsult.sipposorder.ui.home.HomeViewModelFactory
import net.sipconsult.sipposorder.ui.login.LoginViewModelFactory
import net.sipconsult.sipposorder.ui.orders.OrderViewModelFactory
import net.sipconsult.sipposorder.ui.orders.details.OrderDetailViewModelFactory
import net.sipconsult.sipposorder.ui.product.ProductViewModelFactory
import net.sipconsult.sipposorder.ui.settings.SettingsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SipPosApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SipPosApplication))

        bind() from singleton { ApplicationDatabase(instance()) }
        bind() from singleton { instance<ApplicationDatabase>().userDao() }
        bind() from singleton { instance<ApplicationDatabase>().productsDao() }
        bind() from singleton { instance<ApplicationDatabase>().productCategoriesDao() }
        bind() from singleton { instance<ApplicationDatabase>().paymentMethodDao() }
        bind() from singleton { instance<ApplicationDatabase>().locationDao() }
        bind() from singleton { instance<ApplicationDatabase>().salesAgentDao() }

        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { SipShopApiService(instance()) }

        bind<ProductNetworkDataSource>() with singleton {
            ProductNetworkDataSourceImpl(
                instance()
            )
        }
        bind<ProductLocalDataSource>() with singleton {
            ProductLocalDataSourceImpl(
                instance()
            )
        }

//        bind<ProductCategoryNetworkDataSource>() with singleton {
//            ProductCategoryNetworkDataSourceImpl(
//                instance()
//            )
//        }
//        bind<ProductCategoryLocalDataSource>() with singleton {
//            ProductCategoryLocalDataSourceImpl(
//                instance()
//            )
//        }


//        bind<TransactionLocalDataSource>() with singleton {
//            TransactionLocalDataSourceImpl()
//        }
//        bind<TransactionNetworkDataSource>() with singleton {
//            TransactionNetworkDataSourceImpl(
//                instance()
//            )
//        }


        bind<LocationLocalDataSource>() with singleton {
            LocationLocalDataSourceImpl(
                instance()
            )
        }
        bind<LocationNetworkDataSource>() with singleton {
            LocationNetworkDataSourceImpl(
                instance()
            )
        }

        bind<OrderLocalDataSource>() with singleton {
            OrderLocalDataSourceImpl(
            )
        }
        bind<OrderNetworkDataSource>() with singleton {
            OrderNetworkDataSourceImpl(
                instance()
            )
        }

        bind<SalesAgentLocalDataSource>() with singleton {
            SalesAgentLocalDataSourceImpl(
                instance()
            )
        }
        bind<SalesAgentNetworkDataSource>() with singleton {
            SalesAgentNetworkDataSourceImpl(
                instance()
            )
        }

        bind<UserDataSource>() with singleton {
            UserDataSourceImpl(
                instance(),
                instance()
            )
        }



        bind<ProductRepository>() with singleton {
            ProductRepositoryImpl(
                instance(),
                instance()
            )
        }

//        bind<ProductCategoryRepository>() with singleton {
//            ProductCategoryRepositoryImpl(
//                instance(),
//                instance()
//            )
//        }

//        bind<TransactionRepository>() with singleton {
//            TransactionRepositoryImpl(
//                instance(),
//                instance()
//            )
//        }


        bind<LocationRepository>() with singleton {
            LocationRepositoryImpl(
                instance(),
                instance()
            )
        }

        bind<OrderRepository>() with singleton {
            OrderRepositoryImpl(
                instance(),
                instance()
            )
        }

        bind<SalesAgentRepository>() with singleton {
            SalesAgentRepositoryImpl(
                instance(),
                instance()
            )
        }

        bind<UserRepository>() with singleton {
            UserRepositoryImpl(
                instance(),
                this@SipPosApplication
            )
        }

        bind<LocationProvider>() with singleton {
            LocationProviderImpl(this@SipPosApplication)
        }
        bind<PosNumberProvider>() with singleton {
            PosNumberProviderImpl(this@SipPosApplication)
        }

        bind() from provider { HomeViewModelFactory() }
//        bind() from provider { ProductCategoryViewModelFactory(instance()) }
        bind() from provider { ProductViewModelFactory(instance(), instance()) }
        bind() from provider { OrderViewModelFactory(instance(), instance()) }
        bind() from provider { OrderDetailViewModelFactory(instance(), instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
        bind() from provider { SettingsViewModelFactory(instance()) }

        bind() from provider {
            SharedViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance(),
                this@SipPosApplication
            )
        }


//        bind() from provider { SettingsViewModelFactory(instance()) }


//        bind() from provider { SalesTransactionViewModelFactory(instance(), instance()) }

    }
}