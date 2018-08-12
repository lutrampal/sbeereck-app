import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    bottomContainer: {
        flexDirection: 'row',
        width: '100%',
        height: 'auto',
        padding: 20,
        borderBottomWidth: 1,
        borderBottomColor: '#D9D9D9'
    },
    leftSide: {
        flex: 1,
    },
    rightSide: {
        width: 'auto',
        height: '100%',
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
});