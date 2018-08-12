import { DrawerNavigator, StackNavigator } from 'react-navigation';
import React from 'react';
import Party from '../screens/Party';
import Products from '../screens/Products';
import AddBeer from '../screens/AddBeer';
import Members from '../screens/Members';
import Parameters from '../screens/Parameters';
import Transactions from '../screens/Transactions';
import NewTransaction from '../screens/NewTransaction';
import DrawerContent from '../components/SideMenu/SideMenu';
import MemberTransactions from "../screens/MemberTransactions"

const PartyNavigator = StackNavigator({
    Party: {
        screen: Party,
    },
    AddBeer: {
        screen: AddBeer,
    },
    Transactions: {
        screen: Transactions,
    },
    NewTransaction: {
        screen: NewTransaction,
    }
}, {
    headerMode: 'none',
    navigationOptions: {
        headerVisible: false,
    }
});


const ProductsNavigator = StackNavigator({
    Products: {
        screen: Products,
    },
}, {
    headerMode: 'none',
    navigationOptions: {
        headerVisible: false,
    }
});


const MembersNavigator = StackNavigator({
    Members: {
        screen: Members,
    },
    MemberTransactions: {
        screen: MemberTransactions
    },
}, {
    headerMode: 'none',
    navigationOptions: {
        headerVisible: false,
    }
});


const ParametersNavigator = StackNavigator({
    Parameters: {
        screen: Parameters,
    }
}, {
    headerMode: 'none',
    navigationOptions: {
        headerVisible: false,
    }
});

export default MainNavigator = DrawerNavigator({
    Party: {
        screen: PartyNavigator,
    },
    Products: {
        screen: ProductsNavigator,
    },
    Members: {
        screen: MembersNavigator,
    },
    Parameters: {
        screen: ParametersNavigator,
    }
}, {
    contentComponent: DrawerContent,
    drawerWidth: 250,
    drawerOpenRoute: 'DrawerOpen',
    drawerCloseRoute: 'DrawerClose',
    drawerToggleRoute: 'DrawerToggle',
});