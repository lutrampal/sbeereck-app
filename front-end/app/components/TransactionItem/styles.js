import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
        padding: 20,
        borderBottomWidth: 1,
        borderBottomColor: '#D9D9D9'
    },
    leftSide: {
        flex: 1,
        justifyContent: 'center'
    },
    rightSide: {
        marginLeft: 5,
        width: 'auto',
        height: '100%',
        alignItems: 'flex-end'
    },
    name: {
        fontWeight: 'bold',
        fontSize: 18,
        marginBottom: 2,
    },
    price: {
        alignSelf: 'flex-end',
        fontSize: 18,
        fontStyle: 'italic'
    },
    editButton: {
        marginTop: 5,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ba3f36',
        width: 30,
        height: 30,
        borderRadius: 5,
    }
});